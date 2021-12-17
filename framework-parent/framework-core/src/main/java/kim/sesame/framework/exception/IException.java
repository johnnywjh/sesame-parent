package kim.sesame.framework.exception;


import kim.sesame.framework.entity.IErrorCode;

/**
 * 异常接口
 */
public interface IException {

    IErrorCode getErrorCodeEnum();

    String getMessage();
}
