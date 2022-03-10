package kim.sesame.common.entity;

/**
 * 响应错误码的抽象解耦,  只能枚举继承
 */
public interface IErrorCode {
    /**
     * 枚举自己的方法,不需要实现
     */
    String name();
    int getCode();
    String getMessage();

}
