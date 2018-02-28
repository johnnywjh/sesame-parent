package kim.sesame.framework.web.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sesame.framework.jwt")
public class JWTProperties {

    /**
     * jwt签发者
     */
    private String iss = "0000";//

    /**
     *  加密秘钥
     */
    private String secret = "aaaaaaaaaaaaaaaaaa";

    /**
     * jwt 过期时间 , 单位秒 , 默认7天
     */
    private int expiresSecond = 60 * 60 * 24 * 7;

    /**
     * 会话 过期时间 , 单位秒 , 默认一个小时
     */
    private int sessionTime = 60 * 60;

}
