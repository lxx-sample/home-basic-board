package li.dongpo.home.service.fund;

import li.dongpo.home.model.FundInfo;

import java.util.List;

/**
 * @author dongpo.li
 * @date 2021/5/29
 */
public interface FundPriceFetcher {

    /**
     * 拉取最近几天的基金净值
     *
     * @param days 天数
     * @return 这几天的基金净值信息, 注意去掉非交易日的数据
     */
    List<PriceObject> fetchLatestHistory(FundInfo fundInfo, int days);

    /**
     * 拉取基金最新净值
     * @param fundInfo 基金信息
     * @return
     */
    PriceObject fetchLatestPrice(FundInfo fundInfo);

    /**
     * 定时同步基金净值
     *
     * @param list 拉取到的最近的基金
     */
    void insertYesterdayPrice(FundInfo fundInfo, List<PriceObject> list);

    class PriceObject {

        private String date;
        private String price;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }

}
