package kim.sesame.framework.exception;

import kim.sesame.framework.define.entity.ErrorCodeEnum;
import kim.sesame.framework.entity.IErrorCode;

/**
 * 业务异常基类
 */
public class BizException extends RuntimeException implements IException {

    private static final long serialVersionUID = 1937263904748419090L;

    private IErrorCode errorCodeEnum;

    public BizException(String msg) {
        this(ErrorCodeEnum.BUSINESS, msg);
    }
    public BizException(IErrorCode errorCodeEnum, String msg) {
        this(errorCodeEnum, msg, null);
    }
    public BizException(IErrorCode errorCodeEnum, Throwable cause) {
        this(errorCodeEnum, errorCodeEnum.getMessage(), cause);
    }

    public BizException(IErrorCode errorCodeEnum) {
        this(errorCodeEnum, errorCodeEnum.getMessage(), null);
    }

    public BizException(String msg, Throwable cause) {
        this(ErrorCodeEnum.BUSINESS, msg, cause);
    }

    public BizException(IErrorCode errorCodeEnum, String msg, Throwable cause) {
        super(msg, cause);
        if (errorCodeEnum == null) {
            throw new IllegalArgumentException("errorCode is null");
        }
        this.errorCodeEnum = errorCodeEnum;
    }

    public IErrorCode getErrorCodeEnum() {
        return errorCodeEnum;
    }

}
