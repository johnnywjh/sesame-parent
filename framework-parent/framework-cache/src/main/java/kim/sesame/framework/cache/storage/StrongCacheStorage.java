package kim.sesame.framework.cache.storage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



/**
* 只读缓存存储
*/
public class StrongCacheStorage<K, V> implements ICacheStorage<K, V> {
    /**
     * 
     */
    private volatile Map<K, V> map;
    
    /**
     * 日志
     */
    Log log = LogFactory.getLog(getClass());
    
    public StrongCacheStorage() {
        this.map = new ConcurrentHashMap<K, V>();
    }


    public void set(K key, V value) {
        map.put(key, value);
    }


    @SuppressWarnings("unchecked")
    public V get(K key) {
        return map.get(key);
//        V v = map.get(key);
//        if(v != null) {
//            String re = CacheUtils.toJsonString(v);
//            if (re != null) {
//                V result = null;
//                try {
//                    result = (V) JSON.parse(re);
//                } catch(Exception e) {
//                    LOG.error(e);
//                }
//                return result;
//            }
//        }
//        return null;
    }
    

    public void remove(K key) {
        map.remove(key);
    }
    

    public void clear() {
        map.clear();
    }
    

    public void set(Map<K, V> values) {
        map = values;
    }
    

    @SuppressWarnings("unchecked")
    public Map<K, V> get() {
        return map;
//        Map<K, V> value = null;
//        if(map != null) {
//            value = new ConcurrentHashMap<K, V>();
//            for(Map.Entry<K, V> entry : map.entrySet()) {
//                V v = entry.getValue();
//                String re = CacheUtils.toJsonString(v);
//                if (re != null) {
//                    V result = null;
//                    try {
//                        result = (V) JSON.parse(re);
//                        value.put(entry.getKey(), result);
//                    } catch(Exception e) {
//                        LOG.error(e);
//                    }
//                }
//            }
//        }
//        return value;
    }

    public Boolean exists(K key) {
        return map.containsKey(key);
    }
}
