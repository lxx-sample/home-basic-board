package li.dongpo.home.schedule;

import li.dongpo.home.manager.WebSocketSessionManager;
import li.dongpo.home.model.dto.MessageObject;
import li.dongpo.home.service.MessageSender;
import li.dongpo.home.service.handler.MessageHandlerExecutor;
import li.dongpo.home.service.handler.SystemHandler;
import li.dongpo.home.utils.JsonUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @author dongpo.li
 * @date 2021/5/27
 */
//@Component
public class SampleTask {

    @Scheduled(cron = "*/1 * * * * ?")
    public void start() {
        Set<String> devices = WebSocketSessionManager.getAllDevices();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (String device : devices) {
            Session session = WebSocketSessionManager.get(device);

            Map<String, Object> args = Map.of("panel", new Random().nextInt(2));
            MessageObject messageObject = new MessageObject("/system/handler/panel", args);

            MessageHandlerExecutor.Task task = new MessageHandlerExecutor.Task(messageObject, session);
            MessageHandlerExecutor.submit(task);
        }
    }

}
