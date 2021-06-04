package li.dongpo.home.service.handler;

import li.dongpo.home.annotation.HandlerMapping;
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
@HandlerMapping("/system/handler")
public class SystemHandler {
    private static final Logger logger = LoggerFactory.getLogger(SystemHandler.class);

    @HandlerMapping("/echo")
    public MessageObject echo() {
        return RequestContextHolder.getRequest();
    }

    @HandlerMapping("/uuid")
    public MessageObject uuid() {
        return RequestContextHolder.getRequest();
    }

    @HandlerMapping("/datetime")
    public MessageObject datetime() {
        return RequestContextHolder.getRequest();
    }

    @HandlerMapping("/panel")
    public MessageObject panel() {
        return RequestContextHolder.getRequest();
    }

}
