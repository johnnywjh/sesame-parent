package kim.sesame.framework.define.entity;

public class ErrorCodeFactory {

    public static final ErrorCode SUCCESS = new ErrorCode(200,"SUCCESS","成功");
    public static final ErrorCode BUSINESS = new ErrorCode(9001,"BUSINESS","业务异常错误");
    public static final ErrorCode NOT_LOGIN = new ErrorCode(9002,"NOT_LOGIN","用户未登录");
    public static final ErrorCode VALIDATOR = new ErrorCode(9003,"VALIDATOR","数据验证错误");
    public static final ErrorCode SYSTEMERR = new ErrorCode(9999,"SYSTEMERR","系统异常");

}
