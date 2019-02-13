package kim.sesame.framework.mybatis.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;


/**
 * mybatis配置类
 *
 * @author johnny
 * date :  2017年7月6日 下午3:11:56
 * Description:
 */
@Configuration
public class MybatisPlusConfig {

    @Autowired
    private DbProperties druid;

    @Autowired
    public void sqlSessionFactory(SqlSessionFactory sqlSessionFactory, MybatisPlusProperties mybatisPlusProperties) {
        sqlSessionFactory.getConfiguration().setCallSettersOnNulls(true);

        System.out.println(sqlSessionFactory.getConfiguration());
    }


}
