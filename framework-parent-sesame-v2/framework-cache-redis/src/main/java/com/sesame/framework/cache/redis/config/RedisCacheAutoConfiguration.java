package com.sesame.framework.cache.redis.config;

import com.sesame.framework.cache.CacheManager;
import com.sesame.framework.cache.redis.serializer.GenericJackson2JsonRedisSerializer;
import com.sesame.framework.cache.redis.storage.RedisCacheStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis Cache 自动装载
 **/
@Configuration
@ConditionalOnClass(CacheManager.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(CacheProperties.class)
public class RedisCacheAutoConfiguration {
    @Autowired
    private CacheProperties properties;


    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Bean
    @ConditionalOnMissingBean
//    @ConditionalOnBean({RedisTemplate.class})
    public RedisCacheStorage redisCacheStorage(RedisTemplate redisTemplate) {
        RedisCacheStorage redisCacheStorage = new RedisCacheStorage();
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        redisCacheStorage.setRedisTemplate(redisTemplate);
        redisCacheStorage.setExpire(properties.getExpireAfter());
        return redisCacheStorage;
    }
}
