package kim.sesame.framework.cache.redis.exception;


import kim.sesame.framework.exception.GeneralException;

/**
*  查询参数异常
*/
public class RedisCacheStorageException extends GeneralException {

    private static final long serialVersionUID = 4664623827741256267L;
    
    public RedisCacheStorageException(String message) {
        super(message);
    }

    public RedisCacheStorageException(Throwable e) {
        super(e);
    }

    public RedisCacheStorageException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
