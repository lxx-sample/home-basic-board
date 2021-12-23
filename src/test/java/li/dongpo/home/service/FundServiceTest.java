package li.dongpo.home.service;

import com.google.common.base.Splitter;
import li.dongpo.home.BaseTest;
import li.dongpo.home.model.FundTradeHistory;
import li.dongpo.home.repository.FundTradeHistoryRepository;
import li.dongpo.home.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dongpo.li
 * @date 2021/6/1
 */
public class FundServiceTest extends BaseTest {

    @Autowired
    private FundTradeHistoryRepository fundTradeHistoryRepository;

    @Test
    public void test() {
        List<FundTradeHistory> fundTradeHistoryList = fundTradeHistoryRepository.findAll(1L);

        String tradeType_buy = FundTradeHistory.TradeTypeEnum.BUY.name();

        List<FundTradeHistory> buyTradeHistoryList = new LinkedList<>();
        List<FundTradeHistory> sellTradeHistoryList = new LinkedList<>();

        BigDecimal total = BigDecimal.ZERO;

        for (FundTradeHistory fundTradeHistory : fundTradeHistoryList) {
            if (StringUtils.equals(fundTradeHistory.getTradeType(), tradeType_buy)) {
                buyTradeHistoryList.add(fundTradeHistory);

                total = total.add(new BigDecimal(fundTradeHistory.getTradeNumber()));
            } else {
                sellTradeHistoryList.add(fundTradeHistory);

                total = total.subtract(new BigDecimal(fundTradeHistory.getTradeNumber()));
            }
        }

        System.out.println("---------- total ----------");
        System.out.println(total.toPlainString());

        for (FundTradeHistory history : sellTradeHistoryList) {
            if (history.getId() == 7) {
                history.setBuyRef("1, 2, 3, 4");
            }
            if (history.getId() == 11) {
                history.setBuyRef("4, 5, 6, 8");
            }
        }

        Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();

        for (FundTradeHistory history : sellTradeHistoryList) {
            List<String> buyRefList = splitter.splitToList(history.getBuyRef());

            BigDecimal tradeNumber = new BigDecimal(history.getTradeNumber());

            for (String buyRef : buyRefList) {

                Iterator<FundTradeHistory> iterator = buyTradeHistoryList.iterator();
                while (iterator.hasNext()) {
                    FundTradeHistory buy = iterator.next();
                    if (StringUtils.equals(buy.getId() + "", buyRef)) {
                        BigDecimal buyTradeNumber = new BigDecimal(buy.getTradeNumber());

                        if (tradeNumber.compareTo(buyTradeNumber) < 0) {
                            buy.setTradeNumber(buyTradeNumber.subtract(tradeNumber).toPlainString());

                            tradeNumber = BigDecimal.ZERO;
                        } else {
                            tradeNumber = tradeNumber.subtract(buyTradeNumber);

                            iterator.remove();
                        }

                    }
                }

            }


        }

        BigDecimal t = BigDecimal.ZERO;
        for (FundTradeHistory history : buyTradeHistoryList) {
            t = t.add(new BigDecimal(history.getTradeNumber()));
        }


        System.out.println("---------- t ----------");
        System.out.println(t);

        System.out.println(total.compareTo(t));


    }

    private void print(List<FundTradeHistory> list) {
        List<String> collect = list.stream()
                .map(fundTradeHistory -> {
                    if (fundTradeHistory == null) {
                        return null;
                    }

                    return fundTradeHistory.getTradeNumber();
                })
                .collect(Collectors.toList());

        System.out.println(JsonUtils.toJson(collect));
    }
}
