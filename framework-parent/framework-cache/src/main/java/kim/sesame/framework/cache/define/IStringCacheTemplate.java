package kim.sesame.framework.cache.define;

import java.util.concurrent.TimeUnit;

public interface IStringCacheTemplate {

    String STRING_CACHE_BEAN = "i_string_cache_template";

    String get(String key);

    void set(String key, String value, long timeout, TimeUnit unit);

    boolean delete(String key);
}
