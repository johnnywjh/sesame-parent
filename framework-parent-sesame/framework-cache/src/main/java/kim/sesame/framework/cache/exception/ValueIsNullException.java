package kim.sesame.framework.cache.exception;


import kim.sesame.framework.exception.GeneralException;

/**
* @ClassName: ValueIsNullException
* @Description: key存在，value为null
*
*/
public class ValueIsNullException extends GeneralException {

    private static final long serialVersionUID = 932825584009506614L;

    public ValueIsNullException(String message) {
        super(message);
    }

    public ValueIsNullException(Throwable e) {
        super(e);
    }

    public ValueIsNullException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
}
