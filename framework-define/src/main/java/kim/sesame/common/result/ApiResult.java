package kim.sesame.common.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * Web 返回结果
 */
@Setter
@Getter
public class ApiResult<T> extends AbstractResponse {

    /* 是否成功 */
    private boolean success;
    /* 异常类型 */
    private String errorType;
    /* 状态码 */
    private int code;
    /* 信息 */
    private String message;
    /* 返回结果 */
    private T data;

    private String traceId;

    /**
     * 是否需要记录响应日志
     */
    @JsonIgnore
    private boolean needLog;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object other; //其他信息

    public ApiResult() {
        this.success = false;
        this.errorType = "";
        this.code = 0;
        this.message = "";
    }

    /*------------------------------------------------*/
    /*----------------       构建方法    --------------*/
    /*------------------------------------------------*/

    public static ApiResult create() {
        return new ApiResult();
    }

    public ApiResult setSuccess(boolean success) {
        this.success = success;
        this.code = ErrorCodeEnum.SUCCESS.getCode();
        if (success) {
            this.message = "操作成功";
        }
        return this;
    }

    public ApiResult setErrorType(String errorType) {
        this.errorType = errorType;
        return this;
    }

    public ApiResult setCode(int code) {
        this.code = code;
        return this;
    }

    public ApiResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public ApiResult setData(T data) {
        this.data = data;
        return this;
    }

    public ApiResult setNeedLog(boolean needLog) {
        this.needLog = needLog;
        return this;
    }

    public ApiResult setOther(Object other) {
        this.other = other;
        return this;
    }

    public ApiResult setTraceInfo(String traceId) {
        this.traceId = traceId;
        return this;
    }

    @Override
    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

//    @Override
//    public String getDataClassName() {
//        if (this.data != null) {
//            return this.data.getClass().getSimpleName();
//        }
//        return super.getDataClassName();
//    }
}
