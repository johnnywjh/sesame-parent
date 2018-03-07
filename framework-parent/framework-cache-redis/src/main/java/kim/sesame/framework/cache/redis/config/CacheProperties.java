package kim.sesame.framework.cache.redis.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 缓存配置文件
 **/
@Data
@ConfigurationProperties(prefix = "sesame.framework.cache")
public class CacheProperties {
    /**
     * 过期时间, 单位秒, 默认 10 分钟
     */
    private int expirationTime = 60 * 10;

}
