package kim.sesame.common.response;

import lombok.Data;

/**
 * Web 返回结果
 */
@Data
public class Response<T> implements java.io.Serializable {

    /* 是否成功 */
    private boolean success;
    /* 异常类型 */
    private String exceptionType;
    /* 状态码 */
    private int code;
    /* 信息 */
    private String message;
    /* 返回结果 */
    private T result;

//    private Object other; //其他信息

    public Response() {
        this.success = false;
        this.exceptionType = "";
        this.code = 0;
        this.message = "";
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
        return this;
    }

    public Response setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
        return this;
    }

    public Response setCode(int code) {
        this.code = code;
        return this;
    }

    public Response setMessage(String message) {
        this.message = message;
        return this;
    }

    public Response setResult(T result) {
        this.result = result;
        return this;
    }

//    public Response setPage(GPage page) {
//        this.page = page;
//        return this;
//    }
//    public Response setOther(Object other) {
//        this.other = other;
//        return this;
//    }

    @Override
    public String toString() {
        return "Response{" +
                "success=" + success +
                ", exceptionType='" + exceptionType + '\'' +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
