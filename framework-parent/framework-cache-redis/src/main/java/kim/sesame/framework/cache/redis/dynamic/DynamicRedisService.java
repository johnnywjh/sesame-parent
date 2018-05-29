package kim.sesame.framework.cache.redis.dynamic;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * redisTemplate 获取service
 */
public class DynamicRedisService {

    private final static Map<String, DynamicRedisTemplate> drt = new HashMap<>();

    static Map<String, DynamicRedisTemplate> getDrt() {
        return drt;
    }

    public static RedisTemplate getRedisTemplate(String key) {
        return drt.get(key).getRedisTemplate();
    }

    public static StringRedisTemplate getStringRedisTemplate(String key) {
        return drt.get(key).getStringRedisTemplate();
    }

}
