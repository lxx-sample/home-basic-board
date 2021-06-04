package li.dongpo.home.controller;

import li.dongpo.home.manager.WebSocketSessionManager;
import li.dongpo.home.model.dto.MessageObject;
import li.dongpo.home.service.handler.MessageHandlerExecutor;
import li.dongpo.home.service.handler.SystemHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * 首页面板 消息通信
 *
 * @author dongpo.li
 * @date 2021/5/27
 */
@Component
@ServerEndpoint("/web/socket/dashboard")
public class WebSocketDashboardController {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketDashboardController.class);

    @OnOpen
    public void onOpen(Session session) {
        String uuid = UUID.randomUUID().toString();
        logger.info("online, uuid={}", uuid);

        Map<String, Object> args = Map.of("uuid", uuid);
        MessageObject messageObject = new MessageObject("/system/handler/uuid", args);

        MessageHandlerExecutor.Task task = new MessageHandlerExecutor.Task(messageObject, session);
        MessageHandlerExecutor.submit(task);

        WebSocketSessionManager.add(session, uuid);
    }

    @OnClose
    public void onClose(Session session) {
        String uuid = WebSocketSessionManager.get(session);
        logger.info("offline, uuid={}", uuid);
        WebSocketSessionManager.remove(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        String uuid = WebSocketSessionManager.get(session);
        if (logger.isDebugEnabled()) {
            logger.debug("receive message, uuid={}, message={}", uuid, message);
        }

        MessageHandlerExecutor.Task task = new MessageHandlerExecutor.Task(message, session);
        MessageHandlerExecutor.submit(task);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.info("连接异常, 关闭连接: ", throwable);

        try {
            session.close();
        } catch (IOException e) {
            logger.info("连接异常,关闭连接失败, ", e);
        } finally {
            WebSocketSessionManager.remove(session);
        }

    }

}
