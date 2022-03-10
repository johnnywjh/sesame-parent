package kim.sesame.common.exception;


import kim.sesame.common.entity.IErrorCode;

/**
 * 异常接口
 */
public interface IException {

    IErrorCode getErrorCodeEnum();

    String getMessage();
}
