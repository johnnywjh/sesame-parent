package kim.sesame.framework.web.response;

import kim.sesame.framework.web.controller.AbstractController;

/**
 * 各种相应实例的封装
 *
 * @author johnny
 * @date 2017/12/4 20:09
 */
public class ResponseFactory {
    /**
     * 登录失败
     */
    public static Response loginFailure(String message) {
        return Response.create().setExceptionType(AbstractController.ExceptionType.BUSINESS)
                .setErrorCode(AbstractController.ErrorCode.BUSINESS).setMessage(message);
    }
    /**
     * 非法请求
     */
    public static Response illegalRequest(String message) {
        return Response.create().setExceptionType(AbstractController.ExceptionType.VALIDATOR)
                .setErrorCode(AbstractController.ErrorCode.VALIDATOR).setMessage(message);
    }

    /**
     * feign 失败
     */
    public static Response feignException(String message) {
        return Response.create().setExceptionType(AbstractController.ExceptionType.NOTVALID)
                .setErrorCode(AbstractController.ErrorCode.NOTVALID).setMessage("feignException : " + message);
    }


}
