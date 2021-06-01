package li.dongpo.home.service;

import li.dongpo.home.model.dto.MessageObject;
import li.dongpo.home.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.IOException;

/**
 * @author dongpo.li
 * @date 2021/5/28
 */
public class MessageSender {
    private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);

    private final Session session;
    private final MessageObject message;

    public MessageSender(Session session, MessageObject message) {
        this.session = session;
        this.message = message;
    }

    public boolean sendText() {
        if (session == null) {
            logger.error("send message error, session is empty, message={}", message);
            return false;
        }

        try {
            synchronized (session) {
                session.getBasicRemote().sendText(JsonUtils.toJson(message));
            }
            return true;
        } catch (IOException e) {
            logger.error("send message exception, message={}", message, e);
            return false;
        }
    }

}
