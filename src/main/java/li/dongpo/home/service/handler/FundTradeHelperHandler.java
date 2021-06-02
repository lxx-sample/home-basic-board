package li.dongpo.home.service.handler;

import li.dongpo.home.manager.RequestContextHolder;
import li.dongpo.home.model.FundTradeHistory;
import li.dongpo.home.model.dto.MessageObject;
import li.dongpo.home.repository.FundTradeHistoryRepository;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author dongpo.li
 * @date 2021/5/28
 */
@Component
public class FundTradeHelperHandler {
    private static final Logger logger = LoggerFactory.getLogger(FundTradeHelperHandler.class);

    @Autowired
    private FundTradeHistoryRepository fundTradeHistoryRepository;

    public MessageObject calc() {
        MessageObject request = RequestContextHolder.getRequest();

        Map<String, Object> args = request.getArgs();
        long id = MapUtils.getLong(args, "fundInfoId", 0L);

        List<FundTradeHistory> fundTradeHistoryList = fundTradeHistoryRepository.findAll(id);

        // TODO

        Map<String, Object> responseAttributes = Map.of(
                "list", fundTradeHistoryList
        );

        return new MessageObject(FundTradeHelperHandler.class, "calc", responseAttributes);
    }



}
