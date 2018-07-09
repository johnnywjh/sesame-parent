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
     * session 失效时间, 默认0
     */
    private int sesaionTime = 0;
    /**
     * 用户登录信息在redis中的保存时间,单位 分钟,默认 5小时
     */
    private int userLoginSaveTime = 300;
    /**
     * 用户登录的key在cookie中保存的时间,单位分钟,默认30天
     */
    private int userCookieSaveTime = 60 * 24 * 30;

    private int defaultPageSize = 10;

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
    private String resource = "http://www.sesame.kim/zy";

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
    private boolean interceptorAuth = false;

    /**
     * 获取用户,角色,菜单 是否加入缓存
     */
    private boolean userInfoCache = true;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtil.isEmpty(this.iconPath)) {
            this.iconPath = this.resource + "/ico/favicon.ico";
        }
        GPage.DEFAULT_PAGE_SIZE = this.defaultPageSize;
    }
}
