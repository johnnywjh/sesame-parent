package kim.sesame.framework.exception;


/**
 * 异常接口
 *
 * @author johnny
 * @date 2017/10/23 20:15
 */
public interface IException {

    String getErrorCode();

    String getNativeMessage();

    void setErrorArguments(Object... objects);

    Object[] getErrorArguments();

}
