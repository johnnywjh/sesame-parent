package kim.sesame.framework.web.config;

import kim.sesame.framework.web.core.MyUrlRewriteFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

/**
 * web 默认配置
 * 
 * @author johnny
 * date :  2017年9月7日 下午8:18:37
 * Description:
 */
@Configuration
public class WebConfig {

	@Resource
	private Environment env;
	@Resource
	private WebProperties web;

	/**
	 * 配置sessio失效时间
	 * @return  EmbeddedServletContainerCustomizer
	 */
//	@Bean
//	public EmbeddedServletContainerCustomizer containerCustomizer() {
//		return container -> {
//			container.setSessionTimeout(web.getSesaionTime() * 60);// 单位为S
//		};
//	}
//import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;

	/**
	 * java伪静态
	 * app.sesame.web.urlrewrite.autoConfiguration=true
	 * @return  FilterRegistrationBean
	 */
	@Bean
	@ConditionalOnProperty(prefix = "sesame.framework.web", name = "urlrewrite-enabled", havingValue = "true")
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean bean = new FilterRegistrationBean();

		MyUrlRewriteFilter mrf = new MyUrlRewriteFilter();
		//mrf.DEFAULT_WEB_CONF_PATH = web.getUrlrewritePath();
		bean.setFilter(mrf);
		bean.addUrlPatterns(web.getUrlrewriteSuffix());

		return bean;
	}
}