package kim.sesame.framework.cache.redis.exception;


/**
* Redis连接异常
*/
public class RedisConnectionException extends RedisCacheStorageException {

    private static final long serialVersionUID = -4269004402633873780L;
    
    public RedisConnectionException(String message) {
        super(message);
    }

    public RedisConnectionException(Throwable e) {
        super(e);
    }

    public RedisConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
