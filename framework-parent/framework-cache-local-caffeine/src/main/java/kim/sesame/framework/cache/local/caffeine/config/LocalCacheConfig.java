package kim.sesame.framework.cache.local.caffeine.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>java方式：caffeine缓存配置</p>
 * Created by zhezhiyong@163.com on 2017/9/22.
 */
@CommonsLog
@Configuration
@EnableCaching
public class LocalCacheConfig {

    /**
     * 个性化配置缓存
     */
    @Bean
    public CacheManager cacheManager(List<LocalTTlCacheAbstract> cacheConfigs) {
        SimpleCacheManager manager = new SimpleCacheManager();
        //把各个cache注册到cacheManager中，CaffeineCache实现了org.springframework.cache.Cache接口
        ArrayList<CaffeineCache> caches = new ArrayList<>();
        log.info("---- 本地缓存 TTL 配置 -----------------");
        cacheConfigs.forEach(config -> {
            config.getConfigs().forEach(c->{
                log.info(c);
                caches.add(new CaffeineCache(c.getName(),
                        Caffeine.newBuilder().recordStats()
                                .expireAfterWrite(c.getTtl(), c.getTimeUnit())
                                .maximumSize(c.getMaxSize())
                                .build())
                );
            });
        });
        log.info("---------------------");
        manager.setCaches(caches);
        return manager;
    }


}
