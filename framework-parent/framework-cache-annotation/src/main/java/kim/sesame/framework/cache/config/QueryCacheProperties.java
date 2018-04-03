package kim.sesame.framework.cache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 缓存配置文件
 **/
@Data
@Component
@ConfigurationProperties(prefix = "sesame.framework.query.cache")
public class QueryCacheProperties {
    /**
     * 定时失效缓存,全局配置
     * 过期时间, 单位分钟, 默认 10 分钟
     */
    private int invalidTime = 10;

}
