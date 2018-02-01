package kim.sesame.framework.cache.exception;


import kim.sesame.framework.exception.GeneralException;

/**
* @ClassName: ValueIsBlankException
* @Description: key存在，value为空
*
*/
public class ValueIsBlankException extends GeneralException {

    private static final long serialVersionUID = 5536157410092139146L;
    
    public ValueIsBlankException(String message) {
        super(message);
    }

    public ValueIsBlankException(Throwable e) {
        super(e);
    }

    public ValueIsBlankException(String message, Throwable cause) {
        super(message, cause);
    }

}
