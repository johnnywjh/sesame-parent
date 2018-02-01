package kim.sesame.framework.exception;

import java.io.Serializable;

/**
 * 业务异常基类
 *
 * @author johnny-王江海
 * @date 2017/10/23 20:13
 */
public class BusinessException extends RuntimeException implements
        Serializable, IException {

    private static final long serialVersionUID = 1937263904748419090L;

    protected String errCode;
    private String natvieMsg;
    private Object[] arguments;

    public BusinessException() {
    }

    public BusinessException(String msg) {
        super(msg);
    }

    public BusinessException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BusinessException(String code, String msg) {
        super(msg);
        this.errCode = code;
    }

    public BusinessException(String code, String msg, Throwable cause) {
        super(msg, cause);
        this.errCode = code;
    }

    public BusinessException(String code, String msg, String natvieMsg) {
        super(msg);
        this.errCode = code;
        this.natvieMsg = natvieMsg;
    }

    public BusinessException(String code, String msg, String natvieMsg,
                             Throwable cause) {
        super(msg, cause);
        this.errCode = code;
        this.natvieMsg = natvieMsg;
    }

    public BusinessException(String code, Object... args) {
        this.errCode = code;
        this.arguments = args;
    }

    public BusinessException(String code, String msg, Object... args) {
        super(msg);
        this.errCode = code;
        this.arguments = args;
    }

    @Override
    public void setErrorArguments(Object... args) {
        this.arguments = args;
    }

    @Override
    public Object[] getErrorArguments() {
        return this.arguments;
    }

    @Override
    public String getErrorCode() {
        return this.errCode;
    }

    @Override
    public String getNativeMessage() {
        return this.natvieMsg;
    }
}
