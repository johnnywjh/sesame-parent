package kim.sesame.framework.cache.redis.server;

import kim.sesame.common.utils.Argument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CacheServer {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public void invalid(String key) {
        Argument.notEmpty(key, "缓存key不能为空");

        log.debug("缓存失效 key : " + key);
        redisTemplate.delete(key);

    }
}
