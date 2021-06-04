package li.dongpo.home.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author dongpo.li
 * @date 2021/5/28
 */
public class MessageHandlerManager {
    private static final Logger logger = LoggerFactory.getLogger(MessageHandlerManager.class);

    private static final Map<String, InvokerObject> MESSAGE_HANDLER_MAPPING = new ConcurrentHashMap<>();

    public static void put(String path, Object bean, Method method) {
        if (MESSAGE_HANDLER_MAPPING.get(path) != null) {
            throw new RuntimeException("path already exists: " + path);
        }

        MESSAGE_HANDLER_MAPPING.put(path, new InvokerObject(bean, method));
    }

    public static InvokerObject get(String path) {
        return MESSAGE_HANDLER_MAPPING.get(path);
    }

    public static class InvokerObject {
        private final Object bean;
        private final Method method;

        public InvokerObject(Object bean, Method method) {
            this.bean = bean;
            this.method = method;
        }

        public Object getBean() {
            return bean;
        }

        public Method getMethod() {
            return method;
        }
    }

}
