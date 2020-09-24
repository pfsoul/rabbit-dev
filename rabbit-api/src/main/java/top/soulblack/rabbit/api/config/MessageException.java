package top.soulblack.rabbit.api.config;

/**
 * Created by lxf on 2020/9/24
 */
public class MessageException extends RuntimeException {

    private static final long serialVersionUID = -1895236561863232767L;

    public MessageException() {
        super();
    }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageException(Throwable cause) {
        super(cause);
    }
}
