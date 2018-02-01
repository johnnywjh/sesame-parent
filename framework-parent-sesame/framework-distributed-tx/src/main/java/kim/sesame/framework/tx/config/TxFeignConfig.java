package kim.sesame.framework.tx.config;

import com.codingapi.tx.springcloud.feign.TransactionRestTemplateInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * 设置feign的configuration
 *
 * @FeignClient(value = "demo2",configuration = TxFeignConfig.class,fallback = Demo2ClientHystric.class)
 */
public class TxFeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new TransactionRestTemplateInterceptor();
    }

}
