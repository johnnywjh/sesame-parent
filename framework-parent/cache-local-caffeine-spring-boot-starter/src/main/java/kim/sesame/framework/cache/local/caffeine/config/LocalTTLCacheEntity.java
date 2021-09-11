package kim.sesame.framework.cache.local.caffeine.config;

import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * 本地缓存-配置,实体
 */
@Data
public class LocalTTLCacheEntity {
    private String name; // 缓存注解上的名字
    private int ttl = 3600;//过期时间（秒）
    private TimeUnit timeUnit = TimeUnit.SECONDS;// 时间单位默认秒
    private int maxSize = 1000;//最大數量


    public LocalTTLCacheEntity() {
    }

    public LocalTTLCacheEntity(String name) {
        this.name = name;
    }

    public LocalTTLCacheEntity(String name, int ttl) {
        this.name = name;
        this.ttl = ttl;
    }

    public LocalTTLCacheEntity(String name, int ttl, TimeUnit timeUnit) {
        this.name = name;
        this.ttl = ttl;
        this.timeUnit = timeUnit;
    }

    public LocalTTLCacheEntity(String name, int ttl, TimeUnit timeUnit, int maxSize) {
        this.name = name;
        this.ttl = ttl;
        this.timeUnit = timeUnit;
        this.maxSize = maxSize;
    }

    public LocalTTLCacheEntity(String name, int ttl, int maxSize) {
        this.name = name;
        this.ttl = ttl;
        this.maxSize = maxSize;
    }

    @Override
    public String toString() {
        return "LocalTTLCacheEntity{" +
                "name='" + name + '\'' +
                ", ttl=" + ttl +
                ", timeUnit=" + timeUnit +
                ", maxSize=" + maxSize +
                '}';
    }
}
