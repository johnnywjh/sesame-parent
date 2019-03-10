package kim.sesame.framework.db.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.sql.SQLException;

/**
 * 数据源配置
 *
 * @author johnny
 * date :  2017年6月12日 上午11:25:48
 * Description:
 */
@Configuration
public class DataSourcesConfig {

	@Resource
	private DbProperties db;

	/**
	 * db初始化
	 *
	 * @author johnny
	 * date :  2017年6月12日 上午11:26:03
	 * @return 11
	 * @throws SQLException
	 *             DruidDataSource
	 */
	@Bean(name="dataSource")
	@ConfigurationProperties(prefix="spring.datasource")
	@ConditionalOnProperty(prefix = "sesame.framework.db", name = "enabled", havingValue = "true")
	public DruidDataSource dataSource(){
		return new DruidDataSource();
	}

	/**
	 * db监控
	 *
	 * @return
	 */
	@Bean
	@ConditionalOnProperty(prefix = "sesame.framework.db", name = "druid-enabled", havingValue = "true")
	public ServletRegistrationBean dbServlet() {
		ServletRegistrationBean reg = new ServletRegistrationBean();
		reg.setServlet(new StatViewServlet());
		reg.addUrlMappings("/db/*");
		// -- reg.addInitParameter("allow", "127.0.0.1");
		// -- reg.addInitParameter("deny","");
		reg.addInitParameter("loginUsername", db.getDruidLoginName());
		reg.addInitParameter("loginPassword", db.getDruidLoginPwd());
		return reg;
	}

	/**
	 * db监控过滤
	 *
	 * @return
	 */
	@Bean
	@ConditionalOnProperty(prefix = "sesame.framework.db", name = "druid-enabled", havingValue = "true")
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new WebStatFilter());
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/db/*");
		return filterRegistrationBean;
	}
}