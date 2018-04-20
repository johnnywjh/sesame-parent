package kim.sesame.framework.web.cas;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * cas 属性配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "sesame.framework.cas")
public class CasProperties {

    /**
     * 保存在 cookie 里的sessionid 的 key
     */
    private String sessionIdKey = "cas_session_id";

    /**
     * 过期时间, 单位秒, 默认 30 分钟
     */
    private int expirationTime = 60 * 30;

    /**
     * cas 服务器地址
     */
    private String casServiceUrl;

}
