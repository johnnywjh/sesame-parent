package kim.sesame.framework.web.config;

import kim.sesame.framework.web.interceptor.auth.AuthTokenInterceptor;
import kim.sesame.framework.web.interceptor.user.UserInfoInterceptor;
import kim.sesame.framework.web.interceptor.web.SessionInterceptor;
import kim.sesame.framework.web.interceptor.web.WebUserInterceptor;
import kim.sesame.framework.web.util.StringToDateConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class MyWebAppConfigurer implements WebMvcConfigurer {

    @SuppressWarnings("all")
    @Resource
    private WebProperties webProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截

        String[] swaggerArr = {"/error", "/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**","/docs.html"};

        // 初始化用户信息
        if (webProperties.isInterceptorUser()) {
            registry.addInterceptor(new UserInfoInterceptor()).addPathPatterns("/**").excludePathPatterns(swaggerArr);
        }
        // 登录校验
        if (webProperties.isInterceptorLogin()) {
            registry.addInterceptor(new WebUserInterceptor()).addPathPatterns("/**").excludePathPatterns(swaggerArr);
        }
        if (webProperties.isInterceptorSession()) {
            registry.addInterceptor(new SessionInterceptor()).addPathPatterns("/**").excludePathPatterns(swaggerArr);
        }
        // 公钥私钥 认证
        if (webProperties.isInterceptorAuth()) {
            registry.addInterceptor(new AuthTokenInterceptor()).addPathPatterns("/**").excludePathPatterns(swaggerArr);
        }

    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToDateConverter());

    }

}
