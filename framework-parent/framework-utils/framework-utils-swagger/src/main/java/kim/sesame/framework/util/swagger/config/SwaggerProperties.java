package kim.sesame.framework.util.swagger.config;

import kim.sesame.framework.utils.StringUtil;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sesame.framework.swagger")
public class SwaggerProperties implements InitializingBean {

    /**
     * 是否开启swagger
     */
    private boolean enable = false;
    /**
     * 中文转码, 默认开启
     */
    private boolean transcoding = true;

    /**
     * swagger的扫描路径
     */
    private String basePackage;

    private String title;
    private String description;
    private String version;
    private String termsOfServiceUrl;
    private Contact contact;

    @Data
    static class Contact {
        private String name;
        private String url;
        private String email;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.enable == false) {
            return;
        }
        if (StringUtil.isEmpty(basePackage)) {
            throw new NullPointerException("如果开启swagger, 请配置扫描路径");
        }
    }
}