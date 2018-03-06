package kim.sesame.framework.cache.provider;

import java.util.Map;



/**
* 批量加载缓存接口
*/
public interface IBatchCacheProvider<K, V> extends ICacheProvider<K, V> {
    
	/**
	 * 批量加载数据
	 * get
	 * @return Map
	 * @since: 0.6
	 */
    Map<K, V> get();
}
