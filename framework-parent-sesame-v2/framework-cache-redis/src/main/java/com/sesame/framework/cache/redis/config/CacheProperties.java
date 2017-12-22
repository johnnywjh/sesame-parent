package com.sesame.framework.cache.redis.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 缓存配置文件
 **/
@ConfigurationProperties(prefix = "framework.cache")
public class CacheProperties {
    /**
     * 过期时间
     */
    private int expireAfter = 60 * 10;


    public int getExpireAfter() {
        return expireAfter;
    }

    public void setExpireAfter(int expireAfter) {
        this.expireAfter = expireAfter;
    }
}
