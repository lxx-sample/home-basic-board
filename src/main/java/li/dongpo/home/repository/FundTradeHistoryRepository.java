package li.dongpo.home.repository;

import li.dongpo.home.model.FundTradeHistory;

import java.util.List;

/**
 * @author dongpo.li
 * @date 2021/5/27
 */
public interface FundTradeHistoryRepository {

    /**
     * 查询某基金的所有交易记录
     * @param fundInfoId
     * @return
     */
    List<FundTradeHistory> findAll(long fundInfoId);

}
