package com.sesame.framework.cache.provider;

import java.util.Date;


/**
* @ClassName: ICacheProvider
* @Description: Cache数据提供接口
*
* @param <K>
* @param <V>
*/
public interface ICacheProvider<K, V> {
    /**
     * 获取最后修改时间
     * @return
     * @see
     */
    Date getLastModifyTime();
}
