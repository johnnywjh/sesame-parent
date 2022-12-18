package kim.sesame.framework.util.swagger.config;


import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import kim.sesame.common.utils.StringUtil;
import kim.sesame.common.web.controller.ISwagger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/*
kim:
  swagger:
    enable: true
    basePackages:
      - com.malldemo.user.facade.controller.demo
    title: xxxx系统
#    description: descriptionXXX
#    version: v1.0
#    termsOfServiceUrl: http://termsOfServiceUrlXXXX
#    contact:
#      name: 联系人
#      url: http://xxxxxxx.com
#      email: xxxxxxx@qq.com
 */

/**
 * 参考文档 https://blog.csdn.net/m0_46357847/article/details/126614004
 */
@Configuration
@EnableOpenApi
@ConditionalOnProperty(prefix = "kim.swagger", name = "enable", havingValue = "true")
public class Swagger3 implements ISwagger {

    @Autowired
    private SwaggerProperties swagger;

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(basePackage(swagger.getBasePackages()))
                //                .apis(RequestHandlerSelectors.basePackage(swagger.getBasePackage())) // 扫描的路径
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        ApiInfoBuilder bean = new ApiInfoBuilder();
        if (StringUtils.isNotEmpty(swagger.getTitle())) {
            String val = swagger.getTitle();
            val = StringUtil.transcoding(val);
            bean.title(val);
        }
        if (StringUtils.isNotEmpty(swagger.getDescription())) {
            String val = swagger.getDescription();
            val = StringUtil.transcoding(val);
            bean.description(val);
        }
        if (StringUtils.isNotEmpty(swagger.getTermsOfServiceUrl())) {
            bean.termsOfServiceUrl(swagger.getTermsOfServiceUrl());
        }
        if (StringUtils.isNotEmpty(swagger.getVersion())) {
            bean.version(swagger.getVersion());
        }
        if (swagger.getContact() != null) {
            bean.contact(new Contact(swagger.getContact().getName(), swagger.getContact().getUrl(), swagger.getContact().getEmail()));
        }

        return bean.build();
    }

    /**
     * 解决springboot2.6 和springfox不兼容问题
     * @return
     */
    @Bean
    public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
                    customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
                }
                return bean;
            }

            private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
                List<T> copy = mappings.stream()
                        .filter(mapping -> mapping.getPatternParser() == null)
                        .collect(Collectors.toList());
                mappings.clear();
                mappings.addAll(copy);
            }

            @SuppressWarnings("unchecked")
            private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
                try {
                    Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
                    field.setAccessible(true);
                    return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
    }


    // 重写basePackage方法，使能够实现多包访问，复制贴上去
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
