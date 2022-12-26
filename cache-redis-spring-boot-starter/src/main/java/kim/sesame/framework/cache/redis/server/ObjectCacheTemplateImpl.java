package kim.sesame.framework.cache.redis.server;

import kim.sesame.framework.cache.define.IObjectCacheTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service//(value = IObjectCacheTemplate.STRING_CACHE_BEAN)
public class ObjectCacheTemplateImpl implements IObjectCacheTemplate {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    @Override
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

}
