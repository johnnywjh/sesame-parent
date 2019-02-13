package kim.sesame.framework.mybatis.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.annotation.Resource;
import javax.sql.DataSource;



/**
 * mybatis配置类
 * 
 * @author johnny
 * date :  2017年7月6日 下午3:11:56
 * Description:
 */
@Configuration
public class MybatisConfig{
	@Resource(name = "dataSource")
	DataSource dataSource;

	@Resource
	private DbProperties druid;

	/**
	 * 可以通过这个类,详细配置mybatis
	 * 
	 * @return
	 */
	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactoryBean() {

		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);

		// 添加XML目录
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		try {
			bean.setMapperLocations(resolver.getResources(druid.getMapperPath()));
			
			org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
			configuration.setCallSettersOnNulls(true);

			bean.setConfiguration(configuration);
			return bean.getObject();
		} catch (Exception e) {
			throw new RuntimeException("sqlSessionFactory init fail", e);
		}
	}

	/**
	 * 用于实际查询的sql工具,传统dao开发形式可以使用这个,基于mapper代理则不需要注入
	 * 
	 * @param sqlSessionFactory
	 * @return
	 */
//	@Bean(name = "sqlSessionTemplate")
//	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
//		return new SqlSessionTemplate(sqlSessionFactory);
//	}

}
