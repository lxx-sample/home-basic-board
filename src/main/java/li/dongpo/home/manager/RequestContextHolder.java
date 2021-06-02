package li.dongpo.home.manager;

import li.dongpo.home.model.dto.MessageObject;

import javax.websocket.Session;

/**
 * @author dongpo.li
 * @date 2021/6/2
 */
public class RequestContextHolder {

    private static final ThreadLocal<RequestContext> REQUEST_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    private RequestContextHolder() {
    }

    public static void set(Session session, MessageObject request) {
        REQUEST_CONTEXT_THREAD_LOCAL.set(new RequestContext(session, request));
    }

    public static Session getSession() {
        return REQUEST_CONTEXT_THREAD_LOCAL.get().getSession();
    }

    public static MessageObject getRequest() {
        return REQUEST_CONTEXT_THREAD_LOCAL.get().getRequest();
    }

    public static void remove() {
        REQUEST_CONTEXT_THREAD_LOCAL.remove();
    }

    private static final class RequestContext {

        private final Session session;
        private final MessageObject request;
        private MessageObject response;

        public RequestContext(Session session, MessageObject request) {
            this.session = session;
            this.request = request;
        }

        public Session getSession() {
            return session;
        }

        public MessageObject getRequest() {
            return request;
        }

        public MessageObject getResponse() {
            return response;
        }

        public void setResponse(MessageObject response) {
            this.response = response;
        }
    }

}
