package kim.sesame.framework.cache.storage;


/**
* 远程缓存接口
*
* @param <K>
* @param <V>
*/
public interface IRemoteCacheStore<K, V> {
    
    /**
     * 主动向Cache更新指定的数据
     * 
     * @param key key
     * @param value value
     * @return boolean 是否执行成功
     */
    
    boolean set(K key, V value);
    
    /**
     * 主动向Cache更新指定的数据,指定过期时间
     * @param key key
     * @param value value
     * @param exp exp
     * @return boolean 是否执行成功
     */
    boolean set(K key, V value, int exp);
    
    /**
     * 获取缓存
     * 
     * @param key 缓存Key
     * @return 缓存Value
     */
    V get(K key);
    
    /**
     * 删除指定的缓存信息
     * @param key 缓存Key
     */
    void remove(K key);
    
    /**
     * 删除多个key的缓存信息
     * @param keys 动态参数 数组[]
     */
    void removeMulti(K... keys);
    
}
