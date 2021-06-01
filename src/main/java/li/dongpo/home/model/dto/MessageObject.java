package li.dongpo.home.model.dto;

import java.util.Map;

/**
 * @author dongpo.li
 * @date 2021/5/28
 */
public class MessageObject {

    private String handler;
    private String method;
    private Map<String, Object> args;

    public MessageObject() {
    }

    public MessageObject(Class<?> clazz, String method, Map<String, Object> args) {
        this.handler = clazz.getCanonicalName();
        this.method = method;
        this.args = args;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }
}
