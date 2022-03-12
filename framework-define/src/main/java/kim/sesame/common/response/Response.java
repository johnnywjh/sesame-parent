package kim.sesame.common.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Web 返回结果
 */
@Setter
@Getter
public class Response<T> implements java.io.Serializable {

    /* 是否成功 */
    private boolean success;
    /* 异常类型 */
    private String errorType;
    /* 状态码 */
    private int code;
    /* 信息 */
    private String msg;
    /* 返回结果 */
    private T data;


    public Response() {
        this.success = false;
        this.errorType = "";
        this.code = 0;
        this.msg = "";
    }

    /*------------------------------------------------*/
    /*----------------       构建方法    --------------*/
    /*------------------------------------------------*/

    public static Response create() {
        return new Response();
    }

    public Response setSuccess(boolean success) {
        this.success = success;
        this.code = ErrorCodeEnum.SUCCESS.getCode();
        if (success) {
            this.msg = "操作成功";
        }
        return this;
    }

    public Response setErrorType(String errorType) {
        this.errorType = errorType;
        return this;
    }

    public Response setCode(int code) {
        this.code = code;
        return this;
    }

    public Response setMsg(String message) {
        this.msg = message;
        return this;
    }

    public Response setResult(T result) {
        this.data = result;
        return this;
    }


}
