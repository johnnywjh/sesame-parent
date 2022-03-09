package kim.sesame.framework.cache.local.caffeine.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 本地缓存配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "kim.caffeine")
public class CaffeineProperties {

    private int ttlTime = 60;
    private TimeUnit unit = TimeUnit.SECONDS;
    private int initialCapacity = 100;// 初始的缓存空间大小
    private long maximumSize = 1000;// 缓存的最大条数
}
