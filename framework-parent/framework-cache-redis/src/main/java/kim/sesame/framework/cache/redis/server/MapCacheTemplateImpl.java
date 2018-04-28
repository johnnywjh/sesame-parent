package kim.sesame.framework.cache.redis.server;

import kim.sesame.framework.cache.define.IMapCacheTemplate;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@CommonsLog
@Service(value = IMapCacheTemplate.STRING_CACHE_BEAN)
public class MapCacheTemplateImpl implements IMapCacheTemplate {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Long size(String mapKey) {
        return redisTemplate.opsForHash().size(mapKey);
    }

    @Override
    public Set<String> keys(String mapKey) {
        return redisTemplate.opsForHash().keys(mapKey);
    }

    @Override
    public boolean hasKey(String mapKey, String key) {
        return redisTemplate.opsForHash().hasKey(mapKey,key);
    }

    @Override
    public Object get(String mapKey, String key) {
        return redisTemplate.opsForHash().get(mapKey,key);
    }

    @Override
    public List multiGet(String mapKey, Collection<String> hashKeys) {
        return redisTemplate.opsForHash().multiGet(mapKey,hashKeys);
    }

    @Override
    public void put(String mapKey, String key, Object value) {
        redisTemplate.opsForHash().put(mapKey,key,value);
    }

    @Override
    public void putAll(String mapKey, Map map) {
        redisTemplate.opsForHash().putAll(mapKey,map);
    }

    @Override
    public Long delete(String mapKey, String... hashKeys) {
        return redisTemplate.opsForHash().delete(mapKey,hashKeys);
    }

}
