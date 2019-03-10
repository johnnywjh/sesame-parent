package kim.sesame.framework.mybatis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 数据源 属性配置
 * <p>参考地址</p>
 * <p>官网说明 : https://github.com/alibaba/druid/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98</p>
 * <p>参考配置 https://blog.csdn.net/hetaohappy/article/details/51861015</p>
 */
@Data
@Component
@ConfigurationProperties(prefix = "sesame.framework.mybatis")
public class MybatisProperties {

    /**
     * dao.xml 扫描规则配置,尽量不要修改
     */
    private String mapperPath = "classpath*:**/dao/*DaoMapping.xml"; // "classpath*:kim/sesame/**/dao/*.xml";

}
