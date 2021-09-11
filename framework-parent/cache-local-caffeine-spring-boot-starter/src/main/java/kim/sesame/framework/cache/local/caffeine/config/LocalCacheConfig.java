package kim.sesame.framework.cache.local.caffeine.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import kim.sesame.framework.exception.BusinessException;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.MessageFormat;
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
    @SuppressWarnings("all")
    @Bean
    public CacheManager cacheManager(List<LocalTTlCacheAbstract> cacheConfigs) {
        SimpleCacheManager manager = new SimpleCacheManager();
        //把各个cache注册到cacheManager中，CaffeineCache实现了org.springframework.cache.Cache接口
        ArrayList<CaffeineCache> caches = new ArrayList<>();
        log.info("---- 本地缓存 TTL 配置 -----------------");
        List<String> cacheNames = new ArrayList<>();// 保存所有加载的缓存名字
        cacheConfigs.forEach(config -> {
            config.getConfigs().forEach(c -> {
                // 判断是否有重复
                if (cacheNames.contains(c.getName())) {
                    throw new BusinessException(MessageFormat.format("LocalTTLConfig有重复的名称:{0},类名:{1}", c.getName(), config.getClass()));
                } else {
                    cacheNames.add(c.getName());
                    log.info(c);
                    caches.add(new CaffeineCache(c.getName(),
                            Caffeine.newBuilder().recordStats()
                                    .expireAfterWrite(c.getTtl(), c.getTimeUnit())
                                    .maximumSize(c.getMaxSize())
                                    .build())
                    );
                }
            });
        });
        log.info("---------------------");
        manager.setCaches(caches);
        return manager;
    }


}
