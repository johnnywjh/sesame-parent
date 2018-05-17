package kim.sesame.framework.web.jwt;

import kim.sesame.framework.utils.MD5Util;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sesame.framework.jwt")
public class JWTProperties {

    /**
     * jwt签发者,这个没啥用,基本不用改
     */
    private String iss = "0000";//

    /**
     *  加密秘钥,每个项目可以设置一个
     */
    private String secret = "57906A5A3206298979C7D7B9693CA9A8";// MD5Util.encodeByMD5("kim.sesame.jwt")

    /**
     * jwt 过期时间 , 单位秒 , 默认7天
     */
    private int invalidSecond = 60 * 60 * 24 * 7;

}
