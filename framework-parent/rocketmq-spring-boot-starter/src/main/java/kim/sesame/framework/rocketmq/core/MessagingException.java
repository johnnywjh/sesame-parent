package kim.sesame.framework.rocketmq.core;

import org.springframework.core.NestedRuntimeException;

/**
 * 消息异常
 **/
public class MessagingException extends NestedRuntimeException {

    public MessagingException(String msg) {
        super(msg);
    }

    public MessagingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
