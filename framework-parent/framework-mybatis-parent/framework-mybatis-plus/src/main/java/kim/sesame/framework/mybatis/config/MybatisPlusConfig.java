package kim.sesame.framework.mybatis.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;


/**
 * mybatis配置类
 *
 * @author johnny
 * date :  2017年7月6日 下午3:11:56
 * Description:
 */
@Configuration
public class MybatisPlusConfig {

    @SuppressWarnings("all")
    @Autowired
    public void sqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        sqlSessionFactory.getConfiguration().setCallSettersOnNulls(true);
    }


}
