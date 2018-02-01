package kim.sesame.framework.tx.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.codingapi.tx.datasource.relational.LCNTransactionDataSource;
import com.codingapi.tx.springcloud.feign.TransactionRestTemplateInterceptor;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class TXConfig {

    @Autowired
    private Environment env;

    @Bean
    public RequestInterceptor requestInterceptor(){
        return new TransactionRestTemplateInterceptor();
    }

    @Bean
    public LCNTransactionDataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));//用户名
        dataSource.setPassword(env.getProperty("spring.datasource.password"));//密码
        dataSource.setInitialSize(2);
        dataSource.setMaxActive(20);
        dataSource.setMinIdle(0);
        dataSource.setMaxWait(60000);
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestOnBorrow(false);
        dataSource.setTestWhileIdle(true);
        dataSource.setPoolPreparedStatements(false);

        LCNTransactionDataSource dataSourceProxy = new LCNTransactionDataSource();
        dataSourceProxy.setDataSource(dataSource);
        dataSourceProxy.setMaxCount(10);
        return dataSourceProxy;
    }
}
