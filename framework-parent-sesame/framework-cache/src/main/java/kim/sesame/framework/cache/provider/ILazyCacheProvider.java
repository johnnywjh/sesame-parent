package kim.sesame.framework.cache.provider;

import java.util.Date;
import java.util.Map;



/**
* @ClassName: ILazyCacheProvider
* @Description: 延迟加载缓存
*
* @param <K>
* @param <V>
*/
public interface ILazyCacheProvider<K, V> extends ICacheProvider<K, V> {
    /**
     * 加载单个元素
     * get
     * @param key
     * @return
     * @return V
     * @since:
     */
    V get(K key);
    
    /**
     * 加载最近被更新的数据
     * getUpdateObjectMaps
     * @param time
     * @return
     * @return Map<K,V>
     * @since:
     */
    Map<K, V> getUpdateObjectMaps(Date time);
    
    /**
     * 加载传入多个K的数据
     * @param
     * @return
     * @see
     */
    Map<K, V> getUpdateObjectMaps(K... keys);
}
