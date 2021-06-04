package li.dongpo.home.service.handler;

import li.dongpo.home.manager.MessageHandlerManager;
import li.dongpo.home.manager.RequestContextHolder;
import li.dongpo.home.model.dto.MessageObject;
import li.dongpo.home.service.MessageSender;
import li.dongpo.home.utils.JsonUtils;
import li.dongpo.home.utils.SpringContextUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
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

            MessageHandlerManager.InvokerObject invokerObject = MessageHandlerManager.get(messageObject.getPath());
            if (invokerObject == null) {
                logger.error("no such handler: {}", messageObject.getPath());
                return;
            }

            RequestContextHolder.set(session, messageObject);

            try {
                Method method = invokerObject.getMethod();

                Parameter[] parameters = method.getParameters();
                Object[] args = new Object[parameters.length];

                for (int i = 0; i < parameters.length; i++) {
                    Parameter parameter = parameters[i];

                    args[i] = getParameterValue(parameter);
                }

                Object response = method.invoke(invokerObject.getBean(), args);
                if (response instanceof MessageObject) {
                    MessageSender sender = new MessageSender(session, (MessageObject) response);
                    sender.sendText();
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Throwable e) {
                logger.error("handler invoke exception", e);
            } finally {
                RequestContextHolder.remove();
            }

        }

        private Object getParameterValue(Parameter parameter) {
            String parameterName = parameter.getName();

            Object parameterValue;

            // boolean
            if (parameter.getType() == boolean.class) {
                parameterValue = MapUtils.getBooleanValue(messageObject.getArgs(), parameterName);
            } else if (parameter.getType() == boolean.class) {
                parameterValue = MapUtils.getBoolean(messageObject.getArgs(), parameterName);
            }

            // byte
            else if (parameter.getType() == byte.class) {
                parameterValue = MapUtils.getByteValue(messageObject.getArgs(), parameterName);
            } else if (parameter.getType() == Byte.class) {
                parameterValue = MapUtils.getByte(messageObject.getArgs(), parameterName);
            }

            // char
            else if (parameter.getType() == char.class || parameter.getType() == Character.class) {
                String value = MapUtils.getString(messageObject.getArgs(), parameterName);

                if (value == null || value.length() == 0) {
                    parameterValue = null;
                } else {
                    parameterValue = value.charAt(0);
                }
            }

            // short
            else if (parameter.getType() == short.class) {
                parameterValue = MapUtils.getShortValue(messageObject.getArgs(), parameterName);
            } else if (parameter.getType() == Short.class) {
                parameterValue = MapUtils.getShort(messageObject.getArgs(), parameterName);
            }

            // int
            else if (parameter.getType() == int.class) {
                parameterValue = MapUtils.getIntValue(messageObject.getArgs(), parameterName);
            } else if (parameter.getType() == Integer.class) {
                parameterValue = MapUtils.getInteger(messageObject.getArgs(), parameterName);
            }

            // float
            else if (parameter.getType() == float.class) {
                parameterValue = MapUtils.getFloatValue(messageObject.getArgs(), parameterName);
            } else if (parameter.getType() == Float.class) {
                parameterValue = MapUtils.getFloat(messageObject.getArgs(), parameterName);
            }

            // long
            else if (parameter.getType() == long.class) {
                parameterValue = MapUtils.getLongValue(messageObject.getArgs(), parameterName);
            } else if (parameter.getType() == Long.class) {
                parameterValue = MapUtils.getLong(messageObject.getArgs(), parameterName);
            }

            // double
            else if (parameter.getType() == double.class) {
                parameterValue = MapUtils.getDoubleValue(messageObject.getArgs(), parameterName);
            } else if (parameter.getType() == Double.class) {
                parameterValue = MapUtils.getDouble(messageObject.getArgs(), parameterName);
            }

            // number
            else if (parameter.getType() == Number.class) {
                parameterValue = MapUtils.getNumber(messageObject.getArgs(), parameterName);
            }

            // string
            else if (parameter.getType() == String.class) {
                parameterValue = MapUtils.getString(messageObject.getArgs(), parameterName);
            }

            // Map
            else if (parameter.getType() == Map.class) {
                parameterValue = MapUtils.getMap(messageObject.getArgs(), parameterName);
            }

            // other
            else {
                parameterValue = MapUtils.getObject(messageObject.getArgs(), parameterName);
            }

            return parameterValue;
        }
    }

}
