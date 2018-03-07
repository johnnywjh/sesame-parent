package kim.sesame.framework.cache.storage;

import java.util.Map;


/**
* 抽象的数据缓存仓库
*/
public interface ICacheStorage<K, V> {
    
    /**
     * 存放数据
     * 
     * @param key key
     * @param value value
     */
    void set(K key, V value);
    
    /**
     * 
     * @param values value
     */
    void set(Map<K, V> values);

    /**
     * 获取数据
     * @param key key
     * @return value
     */
    V get(K key);
    
    /**
     * 移除指定的数据
     * 
     * @param key key
     */
    void remove(K key);
    
    /**
     * 移除所有的数据
     */
    void clear();
    
    /**
     * 
     * @return map
     */
    Map<K, V> get();

    /**
     * 是否存在
     * @param key key
     * @return boolean
     */
    Boolean exists(K key);
}
