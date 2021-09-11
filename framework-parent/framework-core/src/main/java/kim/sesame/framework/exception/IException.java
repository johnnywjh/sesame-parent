package kim.sesame.framework.exception;


/**
 * 异常接口
 */
public interface IException {

    String getErrorCode();

    String getNativeMessage();

    void setErrorArguments(Object... objects);

    Object[] getErrorArguments();

}
