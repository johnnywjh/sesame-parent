package kim.sesame.framework.web.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import kim.sesame.framework.define.entity.ErrorCodeFactory;
import kim.sesame.framework.entity.GPage;
import kim.sesame.framework.web.context.LogProintContext;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private GPage page;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object other; //其他信息

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
        Response response = new Response();

        LogProintContext logProintContext = LogProintContext.getLogProintContext();
        if (logProintContext != null && logProintContext.getIsIgnore()) {
            logProintContext.setResponse(response);
        }
        return response;
    }

    public Response setSuccess(boolean success) {
        this.success = success;
        this.code = ErrorCodeFactory.SUCCESS.getCode();
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

    public Response setPage(GPage page) {
        this.page = page;
        return this;
    }
    public Response setOther(Object other) {
        this.other = other;
        return this;
    }

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
