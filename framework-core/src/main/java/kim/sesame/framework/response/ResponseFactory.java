package kim.sesame.framework.response;

import kim.sesame.framework.define.entity.ErrorCodeEnum;

/**
 * 各种相应实例的封装
 */
public class ResponseFactory {
    /**
     /**
     * 登录失败
     *
     * @param message 描述
     * @return Response
     */
    public static Response loginFailure(String message) {
        ErrorCodeEnum ece = ErrorCodeEnum.BUSINESS;
        return Response.create().setExceptionType(ece.name()).setCode(ece.getCode()).setMessage(message);
    }
    public static Response notLogin(String message) {
        ErrorCodeEnum ece = ErrorCodeEnum.NOT_LOGIN;
        return Response.create().setExceptionType(ece.name()).setCode(ece.getCode()).setMessage(message);
    }


}
