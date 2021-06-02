package li.dongpo.home.repository;

import li.dongpo.home.BaseTest;
import li.dongpo.home.model.FundTradeHistory;
import li.dongpo.home.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author dongpo.li
 * @date 2021/6/2
 */
public class FundTradeHistoryRepositoryTest extends BaseTest {

    @Autowired
    private FundTradeHistoryRepository fundTradeHistoryRepository;

    @Test
    public void testFindAll() {
        List<FundTradeHistory> fundTradeHistoryList = fundTradeHistoryRepository.findAll(1);

        System.out.println(JsonUtils.toJson(fundTradeHistoryList));
    }

}
