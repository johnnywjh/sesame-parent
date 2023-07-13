package kim.sesame.common.web.context;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.Map;

@Component
@Slf4j
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    private static String currentPath; // 当前项目资源路径,


    /**
     * 获取上下文
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 设置上下文
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;
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

    public static void printStartInfo(){
        printStartInfo(null);
    }
    public static void printStartInfo(String ip){
        try {
            if(StringUtils.isBlank(ip)){
                ip = InetAddress.getLocalHost().getHostAddress();
            }
        } catch (Exception e) {
        }
        String active = getAttributeValue("spring.profiles.active");
        String port = getAttributeValue("server.port");
        String path = getAttributeValue("server.servlet.context-path");
        path = StringUtils.isNotBlank(path) ? path : "";
        log.info("\n----------------------------------------------------------\n\t" +
                "Application is running! Access URLs:\n\t" +
                "spring.profiles.active: \t" + active +"\n\t" +
                "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
                "Swagger文档: \thttp://" + ip + ":" + port + path + "/doc.html\n" +
                "----------------------------------------------------------");
    }


}