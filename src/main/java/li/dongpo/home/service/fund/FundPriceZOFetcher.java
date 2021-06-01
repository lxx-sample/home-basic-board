package li.dongpo.home.service.fund;

import com.fasterxml.jackson.core.type.TypeReference;
import li.dongpo.home.model.FundInfo;
import li.dongpo.home.model.FundPriceHistory;
import li.dongpo.home.repository.FundPriceHistoryRepository;
import li.dongpo.home.utils.HttpUtils;
import li.dongpo.home.utils.JsonUtils;
import li.dongpo.home.utils.SpringContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 中欧基金净值拉取
 *
 * @author dongpo.li
 * @date 2021/5/29
 */
public class FundPriceZOFetcher implements FundPriceFetcher {
    private static final Logger logger = LoggerFactory.getLogger(FundPriceZOFetcher.class);

    @Override
    public List<PriceObject> fetchLatestHistory(FundInfo fundInfo, int days) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String startDate = LocalDate.now().minusDays(days).format(DateTimeFormatter.ISO_LOCAL_DATE);
        String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

        String url = "https://www.zofund.com/fund/FundPerformance/fundGetChart.do";
        url += "?beginDate=" + startDate + "&endDate=" + today;
        url += "&fundcode=" + fundInfo.getCode() + "&isMoneyType=0";

        String body = HttpUtils.get(url);

        List<Map<String, String>> list = JsonUtils.fromJson(body, new TypeReference<>() {
        });
        if (list == null) {
            return List.of();
        }

        return list.stream().map(each -> {
            String date = LocalDate.parse(each.get("xDate"), formatter).format(DateTimeFormatter.ISO_LOCAL_DATE);
            String value = each.get("yValue1");

            PriceObject priceObject = new PriceObject();
            priceObject.setDate(date);
            priceObject.setPrice(value);
            return priceObject;
        }).collect(Collectors.toList());

    }

    @Override
    public PriceObject fetchLatestPrice(FundInfo fundInfo) {
        String url = "http://fundgz.1234567.com.cn/js/" + fundInfo.getCode() + ".js?rt=" + System.currentTimeMillis();
        String body = HttpUtils.get(url);
        body = StringUtils.substringBetween(body, "jsonpgz(", ")");

        PriceObject priceObject = new PriceObject();

        CurrentPriceData currentPriceData = JsonUtils.fromJson(body, CurrentPriceData.class);
        if (currentPriceData == null) {
            logger.error("ERROR");
            return null;
        }

        String date = currentPriceData.getGztime().split(" ")[0];

        priceObject.setPrice(currentPriceData.getGsz());
        priceObject.setDate(date);

        return priceObject;
    }

    @Override
    public void insertYesterdayPrice(FundInfo fundInfo, List<PriceObject> list) {

        String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);

        for (PriceObject priceObject : list) {
            String date = priceObject.getDate();
            String value = priceObject.getPrice();

            if (StringUtils.equals(date, yesterday)) {
                FundPriceHistory history = new FundPriceHistory();
                history.setFundInfoId(fundInfo.getId());
                history.setCode(fundInfo.getCode());
                history.setName(fundInfo.getName());
                history.setTradeDate(date);
                history.setTradingDay(FundPriceHistory.FUND_TRADING_DAY_YES);
                history.setPrice(new BigDecimal(value));

                FundPriceHistoryRepository fundPriceHistoryRepository =
                        SpringContextUtils.getApplicationContext().getBean(FundPriceHistoryRepository.class);

                fundPriceHistoryRepository.insert(history);

                logger.info("基金净值同步成功, code={}, name={}, date={}, price={}", fundInfo.getCode(), fundInfo.getName(), date, value);
            }
        }
    }

    private static class CurrentPriceData {
        private String gsz;
        private String gztime;

        public String getGsz() {
            return gsz;
        }

        public void setGsz(String gsz) {
            this.gsz = gsz;
        }

        public String getGztime() {
            return gztime;
        }

        public void setGztime(String gztime) {
            this.gztime = gztime;
        }
    }
}
