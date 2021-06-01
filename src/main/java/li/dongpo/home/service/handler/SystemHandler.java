package li.dongpo.home.service.handler;

import li.dongpo.home.model.dto.MessageObject;
import li.dongpo.home.service.MessageSender;
import li.dongpo.home.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.Map;

/**
 * @author dongpo.li
 * @date 2021/5/28
 */
@Component
public class SystemHandler {
    private static final Logger logger = LoggerFactory.getLogger(SystemHandler.class);

    public void echo(Session session, MessageObject messageObject) {
        MessageSender sender = new MessageSender(session, messageObject);
        sender.sendText();
    }

    public void uuid(Session session, MessageObject messageObject) {
        MessageSender sender = new MessageSender(session, messageObject);
        sender.sendText();
    }

    public void datetime(Session session, MessageObject messageObject) {
        MessageSender sender = new MessageSender(session, messageObject);
        sender.sendText();
    }

    public void panel(Session session, MessageObject messageObject) {
        MessageSender sender = new MessageSender(session, messageObject);
        sender.sendText();
    }


}
