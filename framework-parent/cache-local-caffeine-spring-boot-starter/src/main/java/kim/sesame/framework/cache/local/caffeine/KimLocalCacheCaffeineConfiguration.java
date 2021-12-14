package kim.sesame.framework.cache.local.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import kim.sesame.framework.cache.local.caffeine.config.CaffeineProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * https://zhuanlan.zhihu.com/p/109226599
 */
@Configuration
@ComponentScan
public class KimLocalCacheCaffeineConfiguration {

    @Bean
    public Cache<String, Object> caffeineCache(CaffeineProperties ca) {
        return Caffeine.newBuilder()
                // 设置最后一次写入或访问后经过固定时间过期
                .expireAfterWrite(ca.getTtlTime(), ca.getUnit())
                // 初始的缓存空间大小
                .initialCapacity(ca.getInitialCapacity())
                // 缓存的最大条数
                .maximumSize(ca.getMaximumSize())
                .build();
    }

}
