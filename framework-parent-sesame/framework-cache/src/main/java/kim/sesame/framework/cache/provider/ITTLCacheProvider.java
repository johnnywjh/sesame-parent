package kim.sesame.framework.cache.provider;


/**
* TTL的缓存数据提供者
*/
public interface ITTLCacheProvider<V> {

    /**
     * 加载单个元素
     * @param key key
     * @return value
     */
    V get(String key);
    
}
