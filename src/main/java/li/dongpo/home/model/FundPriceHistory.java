package li.dongpo.home.model;

import java.math.BigDecimal;

/**
 * @author dongpo.li
 * @date 2021/5/29
 */
public class FundPriceHistory {

    public static final String FUND_TRADING_DAY_YES = "Y";
    public static final String FUND_TRADING_DAY_NO = "N";

    private long id;
    private long fundInfoId;
    /**
     * 基金名称
     */
    private String name;
    /**
     * 基金编号
     */
    private String code;
    /**
     * 价格对应的日期
     */
    private String tradeDate;
    /**
     * 当日净值
     */
    private BigDecimal price;
    /**
     * 是不是交易日,报警的时候要用  YES/NO
     */
    private String tradingDay;
    private String createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFundInfoId() {
        return fundInfoId;
    }

    public void setFundInfoId(long fundInfoId) {
        this.fundInfoId = fundInfoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTradingDay() {
        return tradingDay;
    }

    public void setTradingDay(String tradingDay) {
        this.tradingDay = tradingDay;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
