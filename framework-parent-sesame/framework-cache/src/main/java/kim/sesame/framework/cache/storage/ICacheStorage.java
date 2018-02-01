package kim.sesame.framework.cache.storage;

import java.util.Map;


/**
* @ClassName: ICacheStorage
* @Description: 抽象的数据缓存仓库
*
* @param <K>
* @param <V>
*/
public interface ICacheStorage<K, V> {
    
    /**
     * 存放数据
     * 
     * @param key
     * @param value
     */
    void set(K key, V value);
    
    /**
     * 
     * @param values
     */
    void set(Map<K, V> values);
    
    /**
     * 获取数据
     * 
     * @param key
     */
    V get(K key);
    
    /**
     * 移除指定的数据
     * 
     * @param key
     */
    void remove(K key);
    
    /**
     * 移除所有的数据
     */
    void clear();
    
    /**
     * 
     * @return
     */
    Map<K, V> get();
    
    /**
     * 是否存在
     * @param key
     * @return
     * @see
     */
    Boolean exists(K key);
}
