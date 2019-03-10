package kim.sesame.framework.db.config;

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
     * 是否开启 druid 数据源加载方式
     */
    private boolean druidEnabled = true;
    /**
     * 是否开启 druid 监控
     */
    private boolean druidMonitorEnabled = false;
    private String druidLoginName = "admin";
    private String druidLoginPwd = "123456";

}
