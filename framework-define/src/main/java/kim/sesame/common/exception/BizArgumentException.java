package kim.sesame.common.exception;


/**
 * 业务参数异常
 */
public class BizArgumentException extends RuntimeException {

    public BizArgumentException(String message) {
        super(message);
    }

    public BizArgumentException() {
    }

    public BizArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizArgumentException(Throwable cause) {
        super(cause);
    }


}
