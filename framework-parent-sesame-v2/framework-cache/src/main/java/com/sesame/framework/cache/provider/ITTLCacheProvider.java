package com.sesame.framework.cache.provider;


/**
* @ClassName: ITTLCacheProvider
* @Description: TTL的缓存数据提供者
*
* @param <V>
*/
public interface ITTLCacheProvider<V> {

    /**
     * 加载单个元素
     * get
     * @param key
     * @return
     * @return V
     * @since:
     */
    V get(String key);
    
}
