package kim.sesame.framework.cache.redis.server;

import lombok.Setter;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public class DynamicStringRedisTemplate {

    @Setter
    private StringRedisTemplate stringRedisTemplate;

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }


    public void set(String key, String value, long timeout, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
    }


    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

}
