package kim.sesame.framework.web.context;

import kim.sesame.framework.utils.Ipconfig;
import kim.sesame.framework.utils.StringUtil;
import kim.sesame.framework.web.config.WebProperties;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@CommonsLog
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    private static String currentPath;

    @Autowired
    Environment env;

    /**
     * 获取上下文
     *
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 设置上下文
     *
     * @param applicationContext applicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;

        //初始化当前项目资源路径
        WebProperties web = applicationContext.getBean(WebProperties.class);

        String context_path = env.getProperty("server.context-path");
        if (StringUtil.isNotEmpty(context_path)) {
            SpringContextUtil.currentPath = web.getUserHome() + "/sesame_space" + context_path;
        }
    }

    public static String getCurrentPath() {
        return currentPath;
    }

    /**
     * 通过名字获取上下文中的bean
     *
     * @param name beanName
     * @return object
     */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    /**
     * 通过类型获取上下文中的bean
     *
     * @param requiredType className
     * @return object
     */
    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    /**
     * 获取application 里的属性值
     *
     * @param key key
     * @return str
     */
    public static String getAttributeValue(String key) {
        try {
            return applicationContext.getEnvironment().getProperty(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void printInfo() {
        println("***************");
        Object dataSource = null;
        try {
            dataSource = SpringContextUtil.getBean("dataSource");
        } catch (Exception e) {
        }
        if (dataSource != null) {
            println(">>>>数据源>>>>>" + dataSource.getClass());
        }
        printBase();
    }

    private static void printBase() {
        Environment environment = getApplicationContext().getEnvironment();
        String project_url = "http://127.0.0.1:" + environment.getProperty("server.port") + environment.getProperty("server.context-path");
        println(project_url);
        project_url = "http://" + Ipconfig.getInfo().getLocalip() + ":" + environment.getProperty("server.port") + environment.getProperty("server.context-path");
        println(project_url);
        String profile = environment.getProperty("spring.profiles.active");
        if (StringUtil.isNotEmpty(profile)) {
            println("当前激活的环境文件:" + profile);
        }
        println("当前项目资源路径:SpringContextUtil.getCurrentPath() : " + getCurrentPath());
        println("***************");
    }

    private static void println(Object obj) {
        log.info(obj.toString());
    }

}