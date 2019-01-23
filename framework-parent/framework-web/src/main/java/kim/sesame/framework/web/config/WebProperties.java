package kim.sesame.framework.web.config;

import kim.sesame.framework.entity.GPage;
import kim.sesame.framework.utils.StringUtil;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 系统默认属性
 *
 * @author johnny
 * date :  2017年9月7日 下午7:20:50
 * Description:
 */
@Data
@Component
@ConfigurationProperties(prefix = "sesame.framework.web")
public class WebProperties implements InitializingBean {

    private String userHome = System.getProperty("user.home");
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

    private boolean urlrewriteEnabled = false; // 是否启动 java 伪静态
    private String urlrewriteSuffix = "*.shtml"; // java伪静态的后缀名
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

    private boolean interceptorUser = false;
    private boolean interceptorLogin = false;
    private boolean interceptorSession = false;
    private boolean interceptorAuth = false;

    /**
     * 开启跨域
     */
    private boolean enableCrossDomain = false;

    /**
     * 当前系统编码
     */
    private String sysCode;
    /**
     * 超级管理员角色编号
     */
    private String spuerAdmin = "spuer_admin";

    /**
     * 文件上传地址
     */
    private String uploadUrl = "http://localhost:8072/index/upload_file";
    /**
     * 界面上是否显示头标识
     */
    private boolean showPageHeads = false;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtil.isEmpty(this.iconPath)) {
            this.iconPath = this.basePath + "/resources/ico/icon.png";
        }
        GPage.DEFAULT_PAGE_SIZE = this.defaultPageSize;

        if (StringUtil.isEmpty(this.sysCode)) {
            throw new NullPointerException("请配置 sesame.framework.web.sys-code=@sysCode@");
        }
        if ("@sysCode@".equals(this.sysCode)) {
            throw new NullPointerException("配置中 @sysCode@ 的值未被过滤替换, 请更新maven环境,重新启动,或者手动指定值");
        }

        ProjectConfig.setSysCode(this.sysCode);
        ProjectConfig.setSpuerAdmin(this.spuerAdmin);
        ProjectConfig.setUploadUrl(this.uploadUrl);
    }
}
