package com.sesame.framework.cache.provider;

import java.util.Map;



/**
* @ClassName: IBatchCacheProvider
* @Description: 批量加载缓存接口
*
* @param <K>
* @param <V>
*/
public interface IBatchCacheProvider<K, V> extends ICacheProvider<K, V> {
    
	/**
	 * 批量加载数据
	 * get
	 * @return
	 * @return Map<K,V>
	 * @since: 0.6
	 */
    Map<K, V> get();
}
