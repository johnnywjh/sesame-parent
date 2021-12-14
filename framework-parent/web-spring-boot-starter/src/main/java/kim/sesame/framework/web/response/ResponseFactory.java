package kim.sesame.framework.web.response;

import kim.sesame.framework.define.entity.ErrorCode;
import kim.sesame.framework.define.entity.ErrorCodeFactory;

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
        ErrorCode ece = ErrorCodeFactory.BUSINESS;
        return Response.create().setExceptionType(ece.getName()).setCode(ece.getCode()).setMessage(message);
    }
    public static Response notLogin(String message) {
        ErrorCode ece = ErrorCodeFactory.NOT_LOGIN;
        return Response.create().setExceptionType(ece.getName()).setCode(ece.getCode()).setMessage(message);
    }


}
