package kim.sesame.framework.cache.redis.server;

import kim.sesame.framework.cache.define.IObjectCacheTemplate;
import kim.sesame.framework.cache.define.IStringCacheTemplate;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@CommonsLog
@Service(value = IObjectCacheTemplate.STRING_CACHE_BEAN)
public class ObjectCacheTemplateImpl implements IObjectCacheTemplate {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public void set(String key, Object value, long timeout, TimeUnit unit) {

    }

    @Override
    public Object get(String key, Class classzz) {
        return null;
    }

    @Override
    public void set(String key, String value, long timeout, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    @Override
    public boolean delete(String key) {
        return stringRedisTemplate.delete(key);
    }

}
