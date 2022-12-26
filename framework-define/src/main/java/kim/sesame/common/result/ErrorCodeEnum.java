package kim.sesame.common.result;

import kim.sesame.common.entity.IErrorCode;

/**
 * 响应状态枚举
 */
public enum ErrorCodeEnum implements IErrorCode {

    SUCCESS(200, "成功"),
    NOT_LOGIN(401, "用户未登录"),
    PARAMS_ERR(900, "参数验证错误"),
    BUSINESS(901, "业务异常错误"),

    SYSTEMERR(999, "系统异常");

    private int code;
    private String message;

    ErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
