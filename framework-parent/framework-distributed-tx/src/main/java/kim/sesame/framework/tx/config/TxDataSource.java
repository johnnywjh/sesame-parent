package kim.sesame.framework.tx.config;

import com.alibaba.druid.pool.DruidDataSource;
import kim.sesame.framework.mybatis.config.DruidProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
public class TxDataSource {

    @Autowired
    private Environment env;
    @Resource
    private DruidProperties druid;

    @Bean
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        String username = env.getProperty("spring.datasource.username");
        String password = env.getProperty("spring.datasource.password");
        if(druid.isEncryption()){
            username = druid.decodeStr(username);
            password = druid.decodeStr(password);
        }
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaxActive(druid.getMaxActive());
        dataSource.setInitialSize(druid.getInitialSize());

        dataSource.setMinIdle(druid.getMinIdle());
        dataSource.setMaxWait(druid.getMaxWait());
        dataSource.setValidationQuery("select 'x'");
        dataSource.setTestOnBorrow(false);
        dataSource.setTestWhileIdle(true);
        dataSource.setPoolPreparedStatements(false);
        return dataSource;
    }

}
