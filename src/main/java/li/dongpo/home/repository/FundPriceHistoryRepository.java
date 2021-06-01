package li.dongpo.home.repository;

import li.dongpo.home.model.FundPriceHistory;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author dongpo.li
 * @date 2021/5/27
 */
public interface FundPriceHistoryRepository {

    void insert(FundPriceHistory history);

    /**
     * 查询基金最近几个交易日的净值
     *
     * @param fundInfoId
     * @param days
     * @return
     */
    List<FundPriceHistory> findLatestPrice(long fundInfoId, int days);

    /**
     * 获取几个交易日的基金最小和最大净值
     * @param fundInfoId
     * @param days
     * @return left: 最小值  right: 最大值
     */
    Pair<Double, Double> findExtremePrice(long fundInfoId, int days);

}
