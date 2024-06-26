package kim.sesame.common.web.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "kim.jwt")
public class JWTProperties {

    /**
     *  加密秘钥,每个项目可以设置一个
     */
    private String secret = "57906A5A3206298979C7D7B9693CA9A8";

    /**
     * jwt 过期时间 , 单位秒 , 默认1天, 如果值为0 表示永不过期
     */
    private long invalidSecond = 60 * 60 * 24;

}
