package kim.sesame.framework.mybatis.config;

import kim.sesame.framework.encryption.EncryptionAndDecryption;
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
@ConfigurationProperties(prefix = "sesame.framework.db")
public class DbProperties {

    /**
     * 配置初始连接,默认1
     */
    private int initialSize = 1;
    /**
     * 配置最小连接,默认1
     */
    private int minIdle = 1;
    /**
     * 配置最大连接,默认20
     */
    private int maxActive = 20;
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
    private boolean testWhileIdle = true;
    private boolean testOnBorrow = false;
    private boolean testOnReturn = false;

    /**
     * dao.xml 扫描规则配置,尽量不要修改
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
        EncryptionAndDecryption ead = new EncryptionAndDecryption("mybatis");
        return ead.decodeCBC(str);
    }
}
