package kim.sesame.common.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "kim.aes")
public class AesConfig {

    /**
     * 密钥
     */
    private String key = "T9HOF7pgqKkkFDHc";
    /**
     * iv加盐，按照实际需求添加
     */
    private String iv = "FbrRHExNNbhmvVgh";

}
