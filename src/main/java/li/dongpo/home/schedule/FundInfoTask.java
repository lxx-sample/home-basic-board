package li.dongpo.home.schedule;

import com.fasterxml.jackson.core.type.TypeReference;
import li.dongpo.home.model.FundInfo;
import li.dongpo.home.model.FundPriceHistory;
import li.dongpo.home.repository.FundInfoRepository;
import li.dongpo.home.repository.FundPriceHistoryRepository;
import li.dongpo.home.service.fund.FundPriceFetcher;
import li.dongpo.home.service.fund.FundPriceFetcher.PriceObject;
import li.dongpo.home.service.fund.FundPriceZOFetcher;
import li.dongpo.home.utils.HttpUtils;
import li.dongpo.home.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * @author dongpo.li
 * @date 2021/5/28
 */
@Component
public class FundInfoTask {
    private static final Logger logger = LoggerFactory.getLogger(FundInfoTask.class);

    @Autowired
    private FundInfoRepository fundInfoRepository;
    @Autowired
    private FundPriceHistoryRepository fundPriceHistoryRepository;

    /**
     * 每天早上9点更新昨天的基金净值
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void price() {
        logger.info("FundInfoTask#price start ...");

        List<FundInfo> fundInfoList = fundInfoRepository.findAll();
        for (FundInfo fundInfo : fundInfoList) {
            FundPriceFetcher fundPriceFetcher = handleFundPriceFetcher(fundInfo);
            List<PriceObject> priceObjectList = fundPriceFetcher.fetchLatestHistory(fundInfo, 10);

            fundPriceFetcher.insertYesterdayPrice(fundInfo, priceObjectList);

        }
    }

    private FundPriceFetcher handleFundPriceFetcher(FundInfo fundInfo) {
        if (fundInfo != null) {
            if (fundInfo.getId() == 1) {
                return new FundPriceZOFetcher();
            }
        }
        return null;
    }

    /**
     * 每5分钟拉取一次基金净值然后判断是否需要报警
     */
    @Scheduled(cron = "0 0/5 9-11,13-15 * * ?")
    public void alarm() {
        logger.info("FundInfoTask#alarm start ...");

        String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

        List<FundInfo> fundInfoList = fundInfoRepository.findAll();
        for (FundInfo fundInfo : fundInfoList) {
            FundPriceFetcher fundPriceFetcher = handleFundPriceFetcher(fundInfo);

            PriceObject priceObject = fundPriceFetcher.fetchLatestPrice(fundInfo);
            if (priceObject == null) {
                logger.error("获取基金最新净值失败");
                continue;
            }

            if (StringUtils.equals(today, priceObject.getDate())) {
                // 当天净值,说明今天是交易日
                Pair<Double, Double> extremePrice = fundPriceHistoryRepository.findExtremePrice(fundInfo.getId(), 1);

                BigDecimal min = BigDecimal.valueOf(extremePrice.getLeft());
                BigDecimal max = BigDecimal.valueOf(extremePrice.getRight());

                String threshold = "0";

                // 计算涨跌幅
                BigDecimal priceChangeRate = calcPriceChangeRate(new BigDecimal(priceObject.getPrice()), min);
                if (new BigDecimal(threshold).compareTo(priceChangeRate) <= 0) {
                    logger.info("3日涨跌幅超过{}%", threshold);
                }

            }

        }
    }

    /**
     * 涨跌幅(%)
     *
     * @param current 最新净值
     * @param history 比较的净值
     * @return
     */
    private BigDecimal calcPriceChangeRate(BigDecimal current, BigDecimal history) {
        return current.subtract(history)
                .abs()
                .multiply(BigDecimal.valueOf(100))
                .divide(history, 2, RoundingMode.HALF_DOWN);
    }

}
