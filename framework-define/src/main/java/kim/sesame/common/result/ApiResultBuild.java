package kim.sesame.common.result;


import java.util.List;

/**
 * 各种相应实例的封装
 */
public class ApiResultBuild {

    //单例 普通的成功用公用的对象
    public static class ContextSuccess {
        public final static ApiResult SUCCESS = ApiResult.create().setSuccess(true);
    }

    public static ApiResult success() {
        return ContextSuccess.SUCCESS;
    }

    public static ApiResult success(Object result) {
        return ApiResult.create().setSuccess(true).setData(result);
    }

    public static ApiResult success(Object result, String msg) {
        return ApiResult.create().setSuccess(true).setData(result).setMessage(msg);
    }

    public static ApiResult success(List list) {
        return ApiResult.create().setSuccess(true).setData(list);
    }

    public static ApiResult success(List list, String msg) {
        return ApiResult.create().setSuccess(true).setData(list).setMessage(msg);
    }

    /**
     /**
     * 登录失败
     *
     * @param message 描述
     * @return Response
     */
    public static ApiResult loginFailure(String message) {
        ErrorCodeEnum ece = ErrorCodeEnum.BUSINESS;
        return ApiResult.create().setErrorType(ece.name()).setCode(ece.getCode()).setMessage(message);
    }
    public static ApiResult notLogin(String message) {
        ErrorCodeEnum ece = ErrorCodeEnum.NOT_LOGIN;
        return ApiResult.create().setErrorType(ece.name()).setCode(ece.getCode()).setMessage(message);
    }


}
