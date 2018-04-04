package kim.sesame.framework.cache.redis.server;

import kim.sesame.framework.utils.Argument;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@CommonsLog
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
