package li.dongpo.home.service.handler;

import li.dongpo.home.manager.RequestContextHolder;
import li.dongpo.home.model.dto.MessageObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author dongpo.li
 * @date 2021/5/28
 */
@Component
public class SystemHandler {
    private static final Logger logger = LoggerFactory.getLogger(SystemHandler.class);

    public MessageObject echo() {
        return RequestContextHolder.getRequest();
    }

    public MessageObject uuid() {
        return RequestContextHolder.getRequest();
    }

    public MessageObject datetime() {
        return RequestContextHolder.getRequest();
    }

    public MessageObject panel() {
        return RequestContextHolder.getRequest();
    }


}
