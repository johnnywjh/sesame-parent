package kim.sesame.framework.mybatis.config;

import kim.sesame.framework.encryption.DES3Utils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * mybatis 属性配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "sesame.framework.mybatis")
public class DruidProperties {

    /**
     * 配置最大连接,默认20
     */
    private int maxActive = 20;
    /**
     * 配置初始连接,默认1
     */
    private int initialSize = 1;
    /**
     * 配置最小连接,默认1
     */
    private int minIdle = 1;
    /**
     * 配置等待超时时间,默认60000
     */
    private int maxWait = 60000;
    /**
     * 间隔多久进行检测,关闭空闲连接,默认60000
     */
    private int timeBetweenEvictionRunsMillis = 60000;
    /**
     * 一个连接最小生存时间,默认300000
     */
    private int minEvictableIdleTimeMillis = 300000;

    /**
     * dao.xml 扫描规则配置,尽量不要修改,因为架构里有的模块有公用 dao.xml
     */
    private String mapperPath = "classpath*:**/dao/*DaoMapping.xml"; // "classpath*:kim/sesame/**/dao/*.xml";

    private boolean druidEnabled = false;
    private String druidLoginName = "admin";
    private String druidLoginPwd = "123456";

    /**
     * 数据库密码是否加密
     */
    private boolean encryption = false;

    public String decodeStr(String str) {
        return DES3Utils.decodeCBC(str);
    }
}
