package li.dongpo.home.model.dto;

import li.dongpo.home.model.FundTradeHistory;

import java.math.BigDecimal;

/**
 * @author dongpo.li
 * @date 2021/6/2
 */
public class FundTradeHistoryDto extends FundTradeHistory {

    /**
     * 持有天数
     */
    private long days;
    /**
     * 收益率
     */
    private BigDecimal yield;

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    public BigDecimal getYield() {
        return yield;
    }

    public void setYield(BigDecimal yield) {
        this.yield = yield;
    }
}
