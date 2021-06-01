package li.dongpo.home.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 基金信息管理
 *
 * @author dongpo.li
 * @date 2021/5/27
 */
public class FundInfo {

    private long id;
    /**
     * 基金名称
     */
    private String name;
    /**
     * 基金编码
     */
    private String code;
    /**
     * 认购费率
     */
    private BigDecimal buyChargeRate;
    /**
     * 赎回费率
     */
    private BigDecimal sellChargeRate;
    private String createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public BigDecimal getBuyChargeRate() {
        return buyChargeRate;
    }

    public void setBuyChargeRate(BigDecimal buyChargeRate) {
        this.buyChargeRate = buyChargeRate;
    }

    public BigDecimal getSellChargeRate() {
        return sellChargeRate;
    }

    public void setSellChargeRate(BigDecimal sellChargeRate) {
        this.sellChargeRate = sellChargeRate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
