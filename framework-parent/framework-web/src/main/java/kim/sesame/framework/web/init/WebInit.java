package kim.sesame.framework.web.init;

import kim.sesame.framework.utils.GData;
import kim.sesame.framework.web.config.WebProperties;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServlet;
import java.text.MessageFormat;
import java.util.Date;

/**
 * web 项目启动时初始化
 * ServletComponentScan(basePackageClasses = WebInit.class)
 *
 * @author johnny
 */
@CommonsLog
@WebListener
public class WebInit extends HttpServlet implements ServletContextListener {

    @Resource
    private Environment env;
    @Resource
    private WebProperties web;

    // 服务器关闭时执行
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        System.out.println("************服务器关闭了***************");
    }

    // 服务器启动时执行
    @Override
    public void contextInitialized(ServletContextEvent arg0) {
//        System.out.println();
        log.info("************************************");
        ServletContext application = arg0.getServletContext();

        // 1 项目当前路径
        if (StringUtils.isEmpty(web.getBasePath())) {
            application.setAttribute("basePath", "");
        } else {
            application.setAttribute("basePath", web.getBasePath());
        }

        // 2 资源 zy 的路径,有默认值
        application.setAttribute("resource", web.getResource());

        // 3 项目中访问 图片的地址
        application.setAttribute("fileMapping", web.getFileMapping());
        application.setAttribute("defaultPageSize", web.getDefaultPageSize());
        application.setAttribute("pageReplace", web.getPageReplace());
        application.setAttribute("iconPath", web.getIconPath());
        application.setAttribute("v", new Date().getTime()+"");

        // 当前的环境 spring.profiles.active
        String profilesActive = env.getProperty("spring.profiles.active");
        if (StringUtils.isEmpty(profilesActive)) {
            profilesActive = "";
        }
        application.setAttribute("profilesActive", profilesActive);

        log.info(MessageFormat.format("basePath:{0} , profilesActive:{1} , resource:{2} , fileMapping:{3} , iconPath:{4} , v:{5}",
                application.getAttribute("basePath")
                , application.getAttribute("profilesActive")
                , application.getAttribute("resource")
                , application.getAttribute("fileMapping"
                ), application.getAttribute("iconPath")
                , application.getAttribute("v")
        ));
        log.info("************************************");
//        System.out.println();
    }

}
