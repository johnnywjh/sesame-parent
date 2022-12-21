package kim.sesame.common.response;


import java.util.List;

/**
 * 各种相应实例的封装
 */
public class ResponseBuild {

    //单例 普通的成功用公用的对象
    public static class ContextSuccess {
        public final static Response SUCCESS = Response.create().setSuccess(true);
    }

    public static Response returnSuccess() {
        return ContextSuccess.SUCCESS;
    }

    public static Response returnSuccess(Object result) {
        return Response.create().setSuccess(true).setData(result);
    }

    public static Response returnSuccess(Object result, String msg) {
        return Response.create().setSuccess(true).setData(result).setMessage(msg);
    }

    public static Response returnSuccess(List list) {
        return Response.create().setSuccess(true).setData(list);
    }

    public static Response returnSuccess(List list, String msg) {
        return Response.create().setSuccess(true).setData(list).setMessage(msg);
    }

    /**
     /**
     * 登录失败
     *
     * @param message 描述
     * @return Response
     */
    public static Response loginFailure(String message) {
        ErrorCodeEnum ece = ErrorCodeEnum.BUSINESS;
        return Response.create().setErrorType(ece.name()).setCode(ece.getCode()).setMessage(message);
    }
    public static Response notLogin(String message) {
        ErrorCodeEnum ece = ErrorCodeEnum.NOT_LOGIN;
        return Response.create().setErrorType(ece.name()).setCode(ece.getCode()).setMessage(message);
    }


}
