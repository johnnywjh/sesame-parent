package kim.sesame.framework.define.entity;

import kim.sesame.framework.entity.IErrorCode;

/**
 * 响应状态枚举
 */
public enum ErrorCodeEnum implements IErrorCode {

    SUCCESS(200, "成功"),
    BUSINESS(9001, "业务异常错误"),
    NOT_LOGIN(9002, "用户未登录"),
    VALIDATOR(9003, "数据验证错误"),

    SYSTEMERR(9999, "系统异常");

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
