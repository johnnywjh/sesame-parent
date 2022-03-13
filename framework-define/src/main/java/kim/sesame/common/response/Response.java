package kim.sesame.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * Web 返回结果
 */
@Setter
@Getter
public class Response<T> extends AbstractResponse {

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

    /**
     * 是否需要记录响应日志
     */
    @JsonIgnore
    private boolean needLog;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object other; //其他信息

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

    public Response setData(T data) {
        this.data = data;
        return this;
    }

    public Response setNeedLog(boolean needLog) {
        this.needLog = needLog;
        return this;
    }

    public Response setOther(Object other) {
        this.other = other;
        return this;
    }

    @Override
    public String getDataClassName() {
        if (this.data != null) {
            return this.data.getClass().getSimpleName();
        }
        return super.getDataClassName();
    }
}
