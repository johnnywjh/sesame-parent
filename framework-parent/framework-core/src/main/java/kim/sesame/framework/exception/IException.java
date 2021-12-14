package kim.sesame.framework.exception;


import kim.sesame.framework.define.entity.ErrorCode;

/**
 * 异常接口
 */
public interface IException {

    ErrorCode getErrorCode();

    String getMessage();
}
