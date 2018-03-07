package kim.sesame.framework.cache.provider;

import java.util.Date;
import java.util.Map;



/**
* 延迟加载缓存
*/
public interface ILazyCacheProvider<K, V> extends ICacheProvider<K, V> {
    /**
     * 加载单个元素
     * @param key key
     * @return value
     */
    V get(K key);

    /**
     * 加载最近被更新的数据
     * @param time time
     * @return map
     */
    Map<K, V> getUpdateObjectMaps(Date time);

    /**
     * 加载传入多个K的数据
     * @param keys keys
     * @return map
     */
    Map<K, V> getUpdateObjectMaps(K... keys);
}
