package li.dongpo.home.manager;

import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.StringReader;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dongpo.li
 * @date 2021/5/27
 */
public class WebSocketSessionManager {

    private WebSocketSessionManager() {
    }

    private static final Map<Session, String> SESSION_TO_DEVICE_MAPPING = new ConcurrentHashMap<>();
    private static final Map<String, Session> DEVICE_TO_SESSION_MAPPING = new ConcurrentHashMap<>();

    public static void add(Session session, String id) {
        SESSION_TO_DEVICE_MAPPING.put(session, id);
        DEVICE_TO_SESSION_MAPPING.put(id, session);
    }

    public static void remove(Session session) {
        String uuid = SESSION_TO_DEVICE_MAPPING.get(session);
        remove(session, uuid);
    }

    public static void remove(String uuid) {
        Session session = DEVICE_TO_SESSION_MAPPING.get(uuid);
        remove(session, uuid);
    }

    private static void remove(Session session, String id) {
        SESSION_TO_DEVICE_MAPPING.remove(session);
        DEVICE_TO_SESSION_MAPPING.remove(id);
    }

    public static Session get(String id) {
        return DEVICE_TO_SESSION_MAPPING.get(id);
    }

    public static String get(Session session) {
        return SESSION_TO_DEVICE_MAPPING.get(session);
    }

    public static Set<String> getAllDevices() {
        return DEVICE_TO_SESSION_MAPPING.keySet();
    }

}
