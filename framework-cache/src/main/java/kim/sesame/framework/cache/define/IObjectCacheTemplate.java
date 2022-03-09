package kim.sesame.framework.cache.define;

import java.util.concurrent.TimeUnit;

public interface IObjectCacheTemplate {

//    String STRING_CACHE_BEAN = "i_object_cache_template";

    Object get(String key);

    void set(String key, Object value, long timeout, TimeUnit unit);

    Boolean delete(String key);
}
