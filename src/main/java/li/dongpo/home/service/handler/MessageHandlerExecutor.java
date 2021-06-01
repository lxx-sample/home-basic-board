package li.dongpo.home.service.handler;

import li.dongpo.home.manager.MessageHandlerManager;
import li.dongpo.home.model.dto.MessageObject;
import li.dongpo.home.utils.JsonUtils;
import li.dongpo.home.utils.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author dongpo.li
 * @date 2021/5/28
 */
public class MessageHandlerExecutor {
    private static final Logger logger = LoggerFactory.getLogger(MessageHandlerExecutor.class);

    private static final ExecutorService executor = new ThreadPoolExecutor(1, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(2));

    private MessageHandlerExecutor() {
    }

    public static void submit(Task task) {
        executor.submit(task);
    }

    public static class Task implements Runnable {

        private final Session session;
        private final MessageObject messageObject;

        public Task(String message, Session session) {
            MessageObject messageObject = JsonUtils.fromJson(message, MessageObject.class);
            if (messageObject == null) {
                logger.error("parse message exception, message={}", message);
            }
            this.messageObject = messageObject;
            this.session = session;
        }

        public Task(MessageObject messageObject, Session session) {
            this.messageObject = messageObject;
            this.session = session;
        }

        @Override
        public void run() {
            if (messageObject == null) {
                return;
            }

            Class<?> clazz = MessageHandlerManager.get(messageObject.getHandler());
            if (clazz == null) {
                logger.error("no such handler: {}", messageObject.getHandler());
                return;
            }

            Object handler = SpringContextUtils.getApplicationContext().getBean(clazz);
            try {
                Method method = clazz.getMethod(messageObject.getMethod(), Session.class, MessageObject.class);

                try {
                    method.invoke(handler, session, messageObject);
                } catch (Throwable e) {
                    logger.error("handler invoke exception", e);
                }

            } catch (NoSuchMethodException e) {
                logger.error("fetch handler exception", e);
            }
        }
    }

}
