package li.dongpo.home.service.fund;

import li.dongpo.home.BaseTest;
import li.dongpo.home.model.FundInfo;
import li.dongpo.home.repository.FundInfoRepository;
import li.dongpo.home.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 中欧基金净值拉取
 *
 * @author dongpo.li
 * @date 2021/5/29
 */
public class FundPriceZOFetcherTest extends BaseTest {

    @Autowired
    private FundInfoRepository fundInfoRepository;
    private final FundPriceZOFetcher fundPriceZOFetcher = new FundPriceZOFetcher();

    @Test
    public void testFetchLatestHistory() {
        FundInfo fundInfo = fundInfoRepository.findById(1);
        List<FundPriceFetcher.PriceObject> priceObjectList = fundPriceZOFetcher.fetchLatestHistory(fundInfo, 30);

        System.out.println(JsonUtils.toJson(priceObjectList));
    }

    @Test
    public void testInsertYesterdayPrice() {
        FundInfo fundInfo = fundInfoRepository.findById(1);

        List<FundPriceFetcher.PriceObject> priceObjectList = fundPriceZOFetcher.fetchLatestHistory(fundInfo, 10);

        fundPriceZOFetcher.insertYesterdayPrice(fundInfo, priceObjectList);
    }

    @Test
    public void testFetchLatestPrice() {
        FundInfo fundInfo = fundInfoRepository.findById(1);
        FundPriceFetcher.PriceObject priceObject = fundPriceZOFetcher.fetchLatestPrice(fundInfo);

        System.out.println(JsonUtils.toJson(priceObject));
    }

}
