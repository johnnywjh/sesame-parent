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
	@Primary // 默认数据源
	@Bean(name = "dataSource", destroyMethod = "close")
	public DruidDataSource Construction() throws SQLException {
		DruidDataSource dataSource = new DruidDataSource();

		dataSource.setUrl(environment.getProperty("spring.datasource.url"));
		String username = environment.getProperty("spring.datasource.username");
		String password = environment.getProperty("spring.datasource.password");
		if(db.isEncryption()){
			username = db.decodeStr(username);
			password = db.decodeStr(password);
		}
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		// dataSource.setDriverClassName(environment.getProperty("spring.datasource.driverClassName"));

		// 配置初始连接
		dataSource.setInitialSize(db.getInitialSize());
		// 配置最小连接
		dataSource.setMinIdle(db.getMinIdle());
		// 配置最大连接
		dataSource.setMaxActive(db.getMaxActive());
		// 连接等待超时时间
		dataSource.setMaxWait(db.getMaxWait());
		// 间隔多久进行检测,关闭空闲连接
		dataSource.setTimeBetweenEvictionRunsMillis(db.getTimeBetweenEvictionRunsMillis());
		// 一个连接最小生存时间
		dataSource.setMinEvictableIdleTimeMillis(db.getMinEvictableIdleTimeMillis());
		// 用来检测是否有效的sql
		dataSource.setValidationQuery("select 'x'");
		dataSource.setTestWhileIdle(db.isTestWhileIdle());
		dataSource.setTestOnBorrow(db.isTestOnBorrow());
		dataSource.setTestOnReturn(db.isTestOnReturn());
		// 打开PSCache,并指定每个连接的PSCache大小
		dataSource.setPoolPreparedStatements(true);
		dataSource.setMaxOpenPreparedStatements(20);
		// 配置sql监控的filter
		if (db.isDruidEnabled()) {
			dataSource.setFilters("stat,wall,log4j");
		}
		try {
			dataSource.init();
		} catch (SQLException e) {
			throw new RuntimeException("db datasource init fail");
		}
		return dataSource;
	}

	/**
	 * db监控
	 *
	 * @return
	 */
	@Bean
	@ConditionalOnProperty(prefix = "sesame.framework.mybatis", name = "db-enabled", havingValue = "true")
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
	@ConditionalOnProperty(prefix = "sesame.framework.mybatis", name = "db-enabled", havingValue = "true")
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new WebStatFilter());
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/db/*");
		return filterRegistrationBean;
	}
}