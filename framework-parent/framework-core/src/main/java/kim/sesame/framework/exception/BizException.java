package kim.sesame.framework.exception;

import kim.sesame.framework.define.entity.ErrorCode;
import kim.sesame.framework.define.entity.ErrorCodeFactory;

/**
 * 业务异常基类
 */
public class BizException extends RuntimeException implements IException {

    private static final long serialVersionUID = 1937263904748419090L;

    private ErrorCode errorCode;

    public BizException(String msg) {
        this(ErrorCodeFactory.BUSINESS, msg);
    }

    public BizException(ErrorCode errorCode, String msg) {
        this(errorCode, msg, null);
    }

    public BizException(ErrorCode errorCode, Throwable cause) {
        this(errorCode, errorCode.getMessage(), cause);
    }

    public BizException(ErrorCode errorCode) {
        this(errorCode, errorCode.getMessage(), null);
    }

    public BizException(String msg, Throwable cause) {
        this(ErrorCodeFactory.BUSINESS, msg, cause);
    }

    public BizException(ErrorCode errorCode, String msg, Throwable cause) {
        super(msg, cause);
        if (errorCode == null) {
            throw new IllegalArgumentException("errorCode is null");
        }
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
