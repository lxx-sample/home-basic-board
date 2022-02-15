package li.dongpo.home.service.handler;

import com.google.common.base.Splitter;
import li.dongpo.home.annotation.HandlerMapping;
import li.dongpo.home.manager.RequestContextHolder;
import li.dongpo.home.model.FundTradeHistory;
import li.dongpo.home.model.dto.FundTradeHistoryDto;
import li.dongpo.home.model.dto.MessageObject;
import li.dongpo.home.repository.FundPriceHistoryRepository;
import li.dongpo.home.repository.FundTradeHistoryRepository;
import li.dongpo.home.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author dongpo.li
 * @date 2021/5/28
 */
@Component
@HandlerMapping("/fund/trade/helper")
public class FundTradeHelperHandler {
    private static final Logger logger = LoggerFactory.getLogger(FundTradeHelperHandler.class);

    @Autowired
    private FundTradeHistoryRepository fundTradeHistoryRepository;
    @Autowired
    private FundPriceHistoryRepository fundPriceHistoryRepository;

    @HandlerMapping("/calc")
    public MessageObject calc(long fundInfoId, String latestPrice, String reckonPrice) {
        MessageObject request = RequestContextHolder.getRequest();

        // 所有交易记录
        List<FundTradeHistory> fundTradeHistoryList = fundTradeHistoryRepository.findAll(fundInfoId);
        // 持有份额统计
        BigDecimal totalNumber = BigDecimal.ZERO;

        // 从所有交易记录中筛选持有记录
        List<FundTradeHistory> validlyTradeList = filterValidlyTrade(fundTradeHistoryList);

        List<FundTradeHistoryDto> validlyTradeListResponse = new ArrayList<>(validlyTradeList.size());

        for (FundTradeHistory tradeHistory : validlyTradeList) {
            totalNumber = totalNumber.add(new BigDecimal(tradeHistory.getTradeNumber()));

            FundTradeHistoryDto dto = new FundTradeHistoryDto();
            BeanUtils.copyProperties(tradeHistory, dto);

            LocalDate tradeDate = DateTimeUtils.parseLocalDate(tradeHistory.getTradeDate());
            long days = days(tradeDate);
            dto.setDays(days);

            BigDecimal yield = new BigDecimal(reckonPrice).subtract(tradeHistory.getPrice())
                    .divide(new BigDecimal(latestPrice), 2, RoundingMode.FLOOR);
            dto.setYield(yield);

            validlyTradeListResponse.add(dto);
        }

        Map<String, Object> responseAttributes = Map.of(
//                "avgPrice", calcAvgPrice(validlyTradeList),
                "totalNumber", totalNumber,
                "totalAmount", totalNumber.multiply(new BigDecimal(latestPrice)),
                "validlyTradeList", validlyTradeListResponse
        );

        return new MessageObject(request.getPath(), responseAttributes);
    }

    private BigDecimal calcAvgPrice(List<FundTradeHistory> fundTradeHistoryList) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal tradeNumber = BigDecimal.ZERO;

        for (FundTradeHistory history : fundTradeHistoryList) {
            totalAmount = totalAmount.add(new BigDecimal(history.getTradeAmount()));
            tradeNumber = tradeNumber.add(new BigDecimal(history.getTradeNumber()));
        }

        return totalAmount.divide(tradeNumber, 2, RoundingMode.HALF_DOWN);
    }

    private BigDecimal decideChargeRate(LocalDate tradeDate) {
        long days = days(tradeDate);

        if (days < 7) {
            return BigDecimal.valueOf(1.5);
        } else if (days < 30) {
            return BigDecimal.valueOf(0.5);
        } else {
            return BigDecimal.ZERO;
        }
    }

    private long days(LocalDate tradeDate) {
        LocalDate today = LocalDate.now();
        Duration duration = Duration.between(LocalDateTime.of(tradeDate, LocalTime.MIN), LocalDateTime.of(today, LocalTime.MIN));

        return duration.getSeconds() / TimeUnit.DAYS.toSeconds(1);
    }

    private List<FundTradeHistory> filterValidlyTrade(List<FundTradeHistory> tradeHistoryList) {
        String tradeType_buy = FundTradeHistory.TradeTypeEnum.IN.name();

        List<FundTradeHistory> buyTradeHistoryList = new LinkedList<>();
        List<FundTradeHistory> sellTradeHistoryList = new LinkedList<>();

        // 1. 拆分为买入和卖出记录
        for (FundTradeHistory fundTradeHistory : tradeHistoryList) {
            if (StringUtils.equals(fundTradeHistory.getTradeType(), tradeType_buy)) {
                buyTradeHistoryList.add(fundTradeHistory);
            } else {
                sellTradeHistoryList.add(fundTradeHistory);
            }
        }

        Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();

        // 删除所有卖出的份额对应的记录
        for (FundTradeHistory history : sellTradeHistoryList) {
            List<String> buyRefList = splitter.splitToList(history.getBuyRef());

            BigDecimal tradeNumber = new BigDecimal(history.getTradeNumber());

            for (String buyRef : buyRefList) {

                Iterator<FundTradeHistory> iterator = buyTradeHistoryList.iterator();
                while (iterator.hasNext()) {
                    FundTradeHistory buy = iterator.next();
                    if (StringUtils.equals(buy.getId() + "", buyRef)) { // 匹配上了对应的记录
                        BigDecimal buyTradeNumber = new BigDecimal(buy.getTradeNumber());

                        if (tradeNumber.compareTo(buyTradeNumber) < 0) {
                            buy.setTradeNumber(buyTradeNumber.subtract(tradeNumber).toPlainString());
                            buy.setTradeAmount(new BigDecimal(buy.getTradeAmount()).subtract(tradeNumber).toPlainString());
                            tradeNumber = BigDecimal.ZERO;
                        } else { // 直接删除对应的记录
                            tradeNumber = tradeNumber.subtract(buyTradeNumber);
                            iterator.remove();
                        }

                    }
                }

            }


        }

        return buyTradeHistoryList.stream().collect(Collectors.toUnmodifiableList());
    }



}
