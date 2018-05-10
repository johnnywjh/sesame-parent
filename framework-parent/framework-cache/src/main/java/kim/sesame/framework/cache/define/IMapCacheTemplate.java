package kim.sesame.framework.cache.define;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IMapCacheTemplate {

    String STRING_CACHE_BEAN = "i_map_cache_template";

    Long size(String mapKey);

    Set<String> keys(String mapKey);

    boolean hasKey(String mapKey, String key);

    Object get(String mapKey, String key);

    List multiGet(String mapKey, Collection<String> hashKeys);

    void put(String mapKey, String key, Object value);

    void putAll(String mapKey, Map map);

    Long delete(String mapKey, String... hashKeys);
}
