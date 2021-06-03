package li.dongpo.home.model;

import java.math.BigDecimal;

/**
 * @author dongpo.li
 * @date 2021/6/2
 */
public class FundTradeHistory {

    private long id;
    private long fundInfoId;
    private String code;
    private String name;
    /**
     * 交易类型
     * 买入(BUY)/卖出(SELL)
     *
     * @see TradeTypeEnum#name()
     */
    private String tradeType;
    /**
     * 交易总金额
     */
    private String tradeAmount;
    /**
     * 成交日期
     */
    private String tradeDate;
    /**
     * 确认份额
     */
    private String tradeNumber;
    /**
     * 确认净值
     */
    private BigDecimal price;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(String tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getTradeNumber() {
        return tradeNumber;
    }

    public void setTradeNumber(String tradeNumber) {
        this.tradeNumber = tradeNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public enum TradeTypeEnum {
        BUY, // 申购
        SELL // 赎回
    }
}
