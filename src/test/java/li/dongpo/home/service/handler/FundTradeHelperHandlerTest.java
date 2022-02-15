package li.dongpo.home.service.handler;

import li.dongpo.home.BaseTest;
import li.dongpo.home.manager.RequestContextHolder;
import li.dongpo.home.model.dto.FundTradeHistoryDto;
import li.dongpo.home.model.dto.MessageObject;
import li.dongpo.home.utils.JsonUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author dongpo.li
 * @date 2022/2/15
 */
public class FundTradeHelperHandlerTest extends BaseTest {

    @Resource
    private FundTradeHelperHandler fundTradeHelperHandler;

    @Test
    public void testCalc() {
        MessageObject request = new MessageObject();
        request.setPath("");
        RequestContextHolder.set(null, request);

        MessageObject messageObject = fundTradeHelperHandler.calc(2, "1.01", "0.1");

        Map<String, Object> args = messageObject.getArgs();

        List<FundTradeHistoryDto> validlyTradeList = (List<FundTradeHistoryDto>) args.get("validlyTradeList");

        System.out.println(JsonUtils.toJson(validlyTradeList));
        if (CollectionUtils.isNotEmpty(validlyTradeList)) {
            for (FundTradeHistoryDto dto : validlyTradeList) {
                System.out.println(dto.getYield());
            }
        }

//        System.out.println(JsonUtils.toJson(calc));
    }

}
