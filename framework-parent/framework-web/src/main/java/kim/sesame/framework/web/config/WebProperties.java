package kim.sesame.framework.web.config;

import kim.sesame.framework.entity.GPage;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 系统默认属性
 *
 * @author johnny
 * date :  2017年9月7日 下午7:20:50
 * server.servlet.session.timeout=30
 */
@Data
@Component
@ConfigurationProperties(prefix = "sesame.framework.web")
public class WebProperties implements InitializingBean {

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

    private int defaultPageSize = 10;

    /**
     * 数据共享, 默认true
     * 当设置为  true , 并且多个相同的服务部署在同一个机器上时,
     * 那它们的 当前项目资源路径:SpringContextUtil.getCurrentPath() 会是一样的,
     * 如果需要数据空间单独分开, 那么就设置为false, 此时会在路径后面加上一个端口号
     */
    private boolean dataShare = true;

    private String pageReplace = "~";

    /**
     * 项目路径,如果采用zuul 后,这个要显示改变
     */
    private String basePath = "";

    /**
     * 外部 javascript,css 等框架的地址
     */
    private String resource = "";

    /**
     * 文件,图片映射路径,
     * 使用场景 : 当数据库保存的是相对路径时
     */
    private String fileMapping = "";

    /**
     * 网页上icon 的图标地址
     */
    private String iconPath;
    /**
     * 用户默认头像
     */
    private String userDefaultImageUrl;

    /**
     * 内置拦截器:打印请求日志
     */
    private boolean interceptorPrintReqLog = false;
    /**
     * 内置拦截器:用户信息信息加载
     */
    private boolean interceptorUser = false;
    /**
     * 内置拦截器:固定用户信息用
     */
    private boolean fixedUserEnable = false;
    private String fixedUserAccount;
    /**
     * 内置拦截器:拦截用户登录,非session方式
     */
    private boolean interceptorLogin = false;
    /**
     * 内置拦截器:拦截用户登录 session方式
     */
    private boolean interceptorSession = false;
    /**
     * 内置拦截器:接口安全签名
     */
    private boolean interceptorAuth = false;

    /**
     * 开启跨域
     */
    private boolean enableCrossDomain = false;

    /**
     * 当前系统编码,默认 web, 一般单机系统不需要配置
     */
    private String sysCode = "web";
    /**
     * 超级管理员角色编号
     */
    private String spuerAdmin = "super_admin";
    /**
     * 界面上是否显示头标识
     */
    private boolean showPageHeads = false;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isEmpty(this.iconPath)) {
            this.iconPath = this.basePath + "/icon/icon.png";
        }
        GPage.DEFAULT_PAGE_SIZE = this.defaultPageSize;
        ProjectConfig.setSysCode(this.sysCode);
        ProjectConfig.setSuperAdmin(this.spuerAdmin);
        ProjectConfig.setDebug(debug);
        ProjectConfig.setSystemExceptionMessage(systemExceptionMessage);
    }
}
