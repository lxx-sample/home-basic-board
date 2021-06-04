package li.dongpo.home.model.dto;

import java.util.Map;

/**
 * @author dongpo.li
 * @date 2021/5/28
 */
public class MessageObject {

    private String path;
    private Map<String, Object> args;

    public MessageObject() {
    }

    public MessageObject(String path, Map<String, Object> args) {
        this.path = path;
        this.args = args;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }
}
