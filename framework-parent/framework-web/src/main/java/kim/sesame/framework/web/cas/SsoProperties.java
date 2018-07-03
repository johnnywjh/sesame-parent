package kim.sesame.framework.web.cas;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * sso 属性配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "sesame.framework.sso")
public class SsoProperties {

    /**
     * 保存在 cookie 里的sessionid 的 key
     */
    private String sessionIdKey = "sso_session_id";

    /**
     * sso 服务器地址
     */
    private String ssoServiceUrl;

}
