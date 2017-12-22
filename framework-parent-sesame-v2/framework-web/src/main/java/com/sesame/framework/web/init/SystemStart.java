package com.sesame.framework.web.init;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServlet;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.core.env.Environment;

import com.sesame.framework.utils.StringUtil;
import com.sesame.framework.web.config.WebProperties;

/**
 * 系统启动时执行
 *
 * @author wangjianghai
 */
@CommonsLog
@WebListener
public class SystemStart extends HttpServlet implements ServletContextListener {

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
        System.out.println();
        log.info("");
        log.info("************服务器启动执行方法***************");
        ServletContext application = arg0.getServletContext();

        // 1 项目当前路径
        if (StringUtil.isEmpty(web.getBasePath())) {
            String context_path = env.getProperty("server.context-path");
            application.setAttribute("basePath", context_path);
        } else {
            application.setAttribute("basePath", web.getBasePath());
        }

        // 2 资源 zy 的路径,有默认值
        application.setAttribute("resource", web.getResource());

        // 3 项目中访问 图片的地址
        application.setAttribute("fileMapping", web.getFileMapping());
        application.setAttribute("defaultPageSize", web.getDefaultPageSize());
        application.setAttribute("pageReplace", web.getPageReplace());

        log.info("basePath : " + application.getAttribute("basePath"));
        log.info("resource : " + application.getAttribute("resource"));
        log.info("fileMapping : " + application.getAttribute("fileMapping"));

        log.info("************服务器启动执行方法结束***************");
        log.info("");
        System.out.println();
    }

}
