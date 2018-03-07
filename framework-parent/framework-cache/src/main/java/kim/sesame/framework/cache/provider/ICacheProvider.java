package kim.sesame.framework.cache.provider;

import java.util.Date;


/**
 * Cache数据提供接口
 * @param <K> key
 * @param <V> value
 */
public interface ICacheProvider<K, V> {
    /**
     * 获取最后修改时间
     * @return date
     */
    Date getLastModifyTime();
}
