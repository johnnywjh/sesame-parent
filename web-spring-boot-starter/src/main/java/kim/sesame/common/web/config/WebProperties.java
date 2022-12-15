package kim.sesame.common.web.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 系统默认属性
 * server.servlet.session.timeout=30
 */
@Data
@Component
@ConfigurationProperties(prefix = "kim.web")
public class WebProperties implements InitializingBean {

    private String testkey = "";

    private String userHome = System.getProperty("user.home");
    /**
     * 是否debug 模式,用于屏蔽向前端返回系统异常的详细信息
     */
    private boolean debug = false;
    /**
     * debug=false 时,向前端提示的文字信息
     */
    private String systemExceptionMessage = "系统异常!";
    /**
     * 当前机器的ip和端口信息,例如 192.168.11.100:8080
     * 默认值在 SpringContextUtil.setApplicationContext 方法中
     * 如果是spring cloud 项目,可以设置为 sesame.framework.web.ip-port=${spring.cloud.client.ipAddress}:${server.port}
     */
    private String currentIpPort;
    /**
     * 当前机器的ip, 看currentIpPort的注释信息
     * current-ip=${spring.cloud.client.ipaddress}
     * sesame.framework.web.current-ip=${current-ip}
     */
    private String currentIp;
    /**
     * 用户登录信息在redis中的保存时间,单位 分钟,默认 5小时
     */
    private int userLoginSaveTime = 300;
    /**
     * 用户登录的key在cookie中保存的时间,单位分钟,默认30天
     */
    private int userCookieSaveTime = 60 * 24 * 30;

    /**
     * 开启跨域
     */
    private boolean enableCrossDomain = false;

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
