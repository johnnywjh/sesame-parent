package kim.sesame.framework.cache.redis.dynamic;

import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 多rediss实例
 */
@Data
public class DynamicRedisTemplate {

    private RedisTemplate redisTemplate;
    private StringRedisTemplate stringRedisTemplate;

    public DynamicRedisTemplate(){}

    public DynamicRedisTemplate(RedisTemplate redisTemplate, StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }
}
