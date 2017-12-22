package com.sesame.framework.exception;


/**
 * 异常接口
 *
 * @author johnny-王江海
 * @date 2017/10/23 20:15
 */
public interface IException {

    String getErrorCode();

    String getNativeMessage();

    void setErrorArguments(Object... objects);

    Object[] getErrorArguments();

}
