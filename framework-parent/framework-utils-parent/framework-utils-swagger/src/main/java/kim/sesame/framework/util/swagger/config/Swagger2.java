package kim.sesame.framework.util.swagger.config;


import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import kim.sesame.framework.utils.StringUtil;
import kim.sesame.framework.web.controller.ISwagger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

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
    private SwaggerProperties swagger;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(basePackage(swagger.getBasePackages()))
                //                .apis(RequestHandlerSelectors.basePackage(swagger.getBasePackage())) // 扫描的路径
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        ApiInfoBuilder bean = new ApiInfoBuilder();
        if (StringUtil.isNotEmpty(swagger.getTitle())) {
            String val = swagger.getTitle();
            val = StringUtil.transcoding(val);
            bean.title(val);
        }
        if (StringUtil.isNotEmpty(swagger.getDescription())) {
            String val = swagger.getDescription();
            val = StringUtil.transcoding(val);
            bean.description(val);
        }
        if (StringUtil.isNotEmpty(swagger.getTermsOfServiceUrl())) {
            bean.termsOfServiceUrl(swagger.getTermsOfServiceUrl());
        }
        if (StringUtil.isNotEmpty(swagger.getVersion())) {
            bean.version(swagger.getVersion());
        }
        if (swagger.getContact() != null) {
            bean.contact(new Contact(swagger.getContact().getName(), swagger.getContact().getUrl(), swagger.getContact().getEmail()));
        }

        return bean.build();
    }


    /**
     * 重写basePackage方法，使能够实现多包访问，复制贴上去
     *
     * @param basePackages
     * @return com.google.common.base.Predicate<springfox.documentation.RequestHandler>
     * @author teavamc
     * @date 2019/1/26
     */
    public static Predicate<RequestHandler> basePackage(final List<String> basePackages) {
        return input -> declaringClass(input).transform(handlerPackage(basePackages)).or(true);
    }

    private static Function<Class<?>, Boolean> handlerPackage(final List<String> basePackage) {

        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackage) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }
}
