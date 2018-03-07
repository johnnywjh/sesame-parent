package kim.sesame.framework.mybatis.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

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
	private Environment environment;
	@Resource
	private DruidProperties druidProperties;

	/**
	 * druid初始化
	 * 
	 * @author johnny
	 * date :  2017年6月12日 上午11:26:03
	 * @return 11
	 * @throws SQLException
	 *             DruidDataSource
	 */
	@Primary // 默认数据源
	@Bean(name = "dataSource", destroyMethod = "close")
	public DruidDataSource Construction() throws SQLException {
		DruidDataSource dataSource = new DruidDataSource();

		dataSource.setUrl(environment.getProperty("spring.datasource.url"));
		dataSource.setUsername(environment.getProperty("spring.datasource.username"));
		dataSource.setPassword(environment.getProperty("spring.datasource.password"));
		// dataSource.setDriverClassName(environment.getProperty("spring.datasource.driverClassName"));

		// 配置最大连接
		dataSource.setMaxActive(20);
		// 配置初始连接
		dataSource.setInitialSize(1);
		// 配置最小连接
		dataSource.setMinIdle(1);
		// 连接等待超时时间
		dataSource.setMaxWait(60000);
		// 间隔多久进行检测,关闭空闲连接
		dataSource.setTimeBetweenEvictionRunsMillis(60000);
		// 一个连接最小生存时间
		dataSource.setMinEvictableIdleTimeMillis(300000);
		// 用来检测是否有效的sql
		dataSource.setValidationQuery("select 'x'");
		dataSource.setTestWhileIdle(true);
		dataSource.setTestOnBorrow(false);
		dataSource.setTestOnReturn(false);
		// 打开PSCache,并指定每个连接的PSCache大小
		dataSource.setPoolPreparedStatements(true);
		dataSource.setMaxOpenPreparedStatements(20);
		// 配置sql监控的filter
		if (druidProperties.isDruidEnabled()) {
			dataSource.setFilters("stat,wall,log4j");
		}
		try {
			dataSource.init();
		} catch (SQLException e) {
			throw new RuntimeException("druid datasource init fail");
		}
		return dataSource;
	}

	/**
	 * druid监控
	 * 
	 * @return
	 */
	@Bean
	@ConditionalOnProperty(prefix = "sesame.framework.mybatis", name = "druid-enabled", havingValue = "true")
	public ServletRegistrationBean druidServlet() {
		ServletRegistrationBean reg = new ServletRegistrationBean();
		reg.setServlet(new StatViewServlet());
		reg.addUrlMappings("/druid/*");
		// -- reg.addInitParameter("allow", "127.0.0.1");
		// -- reg.addInitParameter("deny","");
		reg.addInitParameter("loginUsername", druidProperties.getDruidLoginName());
		reg.addInitParameter("loginPassword", druidProperties.getDruidLoginPwd());
		return reg;
	}

	/**
	 * druid监控过滤
	 * 
	 * @return
	 */
	@Bean
	@ConditionalOnProperty(prefix = "sesame.framework.mybatis", name = "druid-enabled", havingValue = "true")
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new WebStatFilter());
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		return filterRegistrationBean;
	}
}