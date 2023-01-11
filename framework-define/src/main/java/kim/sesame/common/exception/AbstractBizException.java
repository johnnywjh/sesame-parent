package kim.sesame.common.exception;

import kim.sesame.common.entity.IErrorCode;

public class AbstractBizException extends RuntimeException implements IException {

    private IErrorCode errorCodeEnum;

    @Override
    public IErrorCode getErrorCodeEnum() {
        return errorCodeEnum;
    }


    /**
     * 业务异常导致请求处理失败，是否需要记错误日志（上报到异常监控）。默认是记WARN日志。
     */
    private boolean needLogError;

    public AbstractBizException(String msg) {
        super(msg);
    }


    public AbstractBizException(String msg, boolean needLogError) {
        this(msg);
        this.needLogError = needLogError;
    }

    public AbstractBizException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public AbstractBizException(String msg, Throwable cause, boolean needLogError) {
        this(msg, cause);
        this.needLogError = needLogError;
    }

    public boolean isNeedLogError() {
        return needLogError;
    }

    public void setNeedLogError(boolean needLogError) {
        this.needLogError = needLogError;
    }
}
