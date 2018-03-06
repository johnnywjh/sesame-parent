package kim.sesame.framework.cache.exception;


import kim.sesame.framework.exception.GeneralException;

/**
* Description: key不存在
*
*/
public class KeyIsNotFoundException extends GeneralException {

    private static final long serialVersionUID = 5165307445946057734L;
    
    public KeyIsNotFoundException(String message) {
        super(message);
    }

    public KeyIsNotFoundException(Throwable e) {
        super(e);
    }

    public KeyIsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
