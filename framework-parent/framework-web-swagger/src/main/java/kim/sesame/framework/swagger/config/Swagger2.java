package kim.sesame.framework.swagger.config;


import kim.sesame.framework.utils.StringUtil;
import kim.sesame.framework.web.controller.ISwagger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger-ui.htm
 */
//@Profile({"dev", "test"})
@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "sesame.framework.swagger", name = "enable", havingValue = "true")
public class Swagger2 implements ISwagger {

    @SuppressWarnings("all")
    @Autowired
    private SwaggerProperties swaggerProperties;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage())) // 扫描的路径
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        ApiInfoBuilder bean = new ApiInfoBuilder();
        if (StringUtil.isNotEmpty(swaggerProperties.getTitle())) {
            String val = swaggerProperties.getTitle();
            val = StringUtil.transcoding(val);
            bean.title(val);
        }
        if (StringUtil.isNotEmpty(swaggerProperties.getDescription())) {
            String val = swaggerProperties.getDescription();
            val = StringUtil.transcoding(val);
            bean.description(val);
        }
        if (StringUtil.isNotEmpty(swaggerProperties.getTermsOfServiceUrl())) {
            bean.termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl());
        }
        if (StringUtil.isNotEmpty(swaggerProperties.getVersion())) {
            bean.version(swaggerProperties.getVersion());
        }
        if (swaggerProperties.getContact() != null) {
            bean.contact(new Contact(swaggerProperties.getContact().getName(), swaggerProperties.getContact().getUrl(), swaggerProperties.getContact().getEmail()));
        }

        return bean.build();
    }

}
