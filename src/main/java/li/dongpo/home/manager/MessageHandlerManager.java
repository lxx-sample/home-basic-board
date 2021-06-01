package li.dongpo.home.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author dongpo.li
 * @date 2021/5/28
 */
public class MessageHandlerManager {
    private static final Logger logger = LoggerFactory.getLogger(MessageHandlerManager.class);

    private static final Map<String, Class<?>> MESSAGE_HANDLER_MAPPING = new ConcurrentHashMap<>();

    private static void put(String handlerName) {
        try {
            Class<?> clazz = Class.forName(handlerName);
            MESSAGE_HANDLER_MAPPING.put(handlerName, clazz);
        } catch (ClassNotFoundException e) {
            logger.error("no such handler: {}", handlerName, e);
        }
    }

    public static Class<?> get(String handlerName) {
        Class<?> clazz = MESSAGE_HANDLER_MAPPING.get(handlerName);

        if (clazz == null) {
            put(handlerName);
        }

        return MESSAGE_HANDLER_MAPPING.get(handlerName);
    }

}
