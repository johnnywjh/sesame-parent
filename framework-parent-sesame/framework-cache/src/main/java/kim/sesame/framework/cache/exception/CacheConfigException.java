package kim.sesame.framework.cache.exception;


import kim.sesame.framework.exception.GeneralException;

/**
* 缓存配置异常
*
*/
public class CacheConfigException extends GeneralException {

	private static final long serialVersionUID = 437438995471412241L;

	public CacheConfigException(String msg) {
        super(msg);
    }
    
}
