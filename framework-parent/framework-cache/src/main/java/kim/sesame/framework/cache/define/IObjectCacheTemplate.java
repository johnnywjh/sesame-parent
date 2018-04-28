package kim.sesame.framework.cache.define;

import java.util.concurrent.TimeUnit;

public interface IObjectCacheTemplate<T> {

    String STRING_CACHE_BEAN = "i_object_cache_template";

    Object get(String key);

    <T> T get(String key,Class<T> classzz);

    void set(String key, T value, long timeout, TimeUnit unit);

    boolean delete(String key);
}
