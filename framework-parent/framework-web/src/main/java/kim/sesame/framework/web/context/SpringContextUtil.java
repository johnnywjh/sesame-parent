package kim.sesame.framework.web.context;

import kim.sesame.framework.utils.GData;
import kim.sesame.framework.utils.Ipconfig;
import kim.sesame.framework.utils.StringUtil;
import kim.sesame.framework.web.config.WebProperties;
import kim.sesame.framework.web.controller.ISwagger;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@CommonsLog
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    private static String currentPath; // 当前项目资源路径,
    private static String projectPath; // 当前项目路径
    private static String currentIpPort; // 当前项目实例
    private static String currentIp; // 当前项目Ip

    @Autowired
    Environment env;
    @Autowired
    WebProperties web;

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

        projectPath = env.getProperty(GData.SPRINGBOOT.contextPath);

        SpringContextUtil.currentPath = web.getUserHome() + "/ars/" + web.getSysCode() + "/data";

        String port = null;
        port = env.getProperty("server.port");
        if (StringUtil.isEmpty(port)) {
            port = "8080";
        }
        if (web.isDataShare() == false) {
            // 给用用的数据添加一个端口号, 防止多个相同的应用数据感染
            SpringContextUtil.currentPath = SpringContextUtil.currentPath + "/" + port;
        }

        // 初始化当前ip信息
        currentIpPort = web.getCurrentIpPort();
        currentIp = web.getCurrentIp();
        if (StringUtil.isEmpty(currentIp)) {
            currentIp = Ipconfig.getInfo().getLocalip();
            web.setCurrentIp(currentIp);
        }
        if (StringUtil.isEmpty(currentIpPort)) {
            currentIpPort = currentIp + ":" + port;
            web.setCurrentIpPort(currentIpPort);
        }
    }

    /**
     * 动态注入bean, 当前bean一定要有无参的构造函数
     *
     * @param beanName bean的名字
     * @param params   bean的属性
     * @param clazz    bean 的clazz
     */
    public static void registerBean(String beanName, Map<String, Object> params, Class clazz) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        for (String key : params.keySet()) {
            beanDefinitionBuilder.addPropertyValue(key, params.get(key));
        }

        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) getApplicationContext().getAutowireCapableBeanFactory();
        beanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
    }

    public static String getCurrentPath() {
        return currentPath;
    }

    public static String getProjectPath() {
        return projectPath;
    }

    public static String getCurrentIpPort() {
        return currentIpPort;
    }

    public static String getCurrentIp() {
        return currentIp;
    }

    /**
     * 通过名字获取上下文中的bean
     *
     * @param name beanName
     * @return object
     */
    public static Object getBean(String name) {
        try {
            return applicationContext.getBean(name);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 通过类型获取上下文中的bean
     *
     * @param requiredType className
     * @return object
     */
    public static <T> T getBean(Class<T> requiredType) {
        try {
            return applicationContext.getBean(requiredType);
        } catch (Exception e) {
            return null;
        }

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
        WebProperties webProperties = getBean(WebProperties.class);
        String port = environment.getProperty("server.port");
        if (StringUtil.isEmpty(port)) {
            port = "8080";
        }
        String basePath = environment.getProperty(GData.SPRINGBOOT.contextPath);
        if (basePath == null) {
            basePath = "";
        }
        String local_project_url = "http://127.0.0.1:" + port + basePath;
        println(local_project_url);
        String ip_project_url = "http://" + webProperties.getCurrentIpPort() + basePath;
        println(ip_project_url);
        String profile = environment.getProperty("spring.profiles.active");
        if (StringUtil.isNotEmpty(profile)) {
            println("当前激活的环境文件:" + profile);
        } else {
            println("当前激活的环境文件: 无 ,如有需要请配置 spring.profiles.active=@profileActive@");
        }
        println("当前项目资源路径:SpringContextUtil.getCurrentPath() : " + getCurrentPath());
        ISwagger iSwagger = SpringContextUtil.getBean(ISwagger.class);
        if (iSwagger != null) {
            String url = "/swagger-ui.html";
            println(local_project_url + url);
            println(ip_project_url + url);
        }
        println("当前应用实例 : " + webProperties.getCurrentIpPort());
        println("***************");
    }

    private static void println(Object obj) {
        log.info(obj.toString());
    }

}