package com.sesame.framework.web.context;

import com.sesame.framework.web.config.WebProperties;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import com.sesame.framework.utils.StringUtil;

public class SpringContextUtil {

	private static Logger log = Logger.getLogger(SpringContextUtil.class);

	private static ApplicationContext applicationContext;
	private static String currentPath;

	/**
	 * 获取上下文
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 设置上下文
	 * 
	 * @param applicationContext
	 */
	public static void setApplicationContext(ApplicationContext applicationContext) {
		SpringContextUtil.applicationContext = applicationContext;

		//初始化当前项目资源路径
		WebProperties web = applicationContext.getBean(WebProperties.class);
		Environment env =  applicationContext.getBean(Environment.class);

		String context_path = env.getProperty("server.context-path");
		if (StringUtil.isNotEmpty(context_path)) {
			setCurrentPath(web.getUserHome()+"/sesame_space"+context_path);
		}
	}

	public static String getCurrentPath() {
		return currentPath;
	}

	public static void setCurrentPath(String currentPath) {
		SpringContextUtil.currentPath = currentPath;
	}

	/**
	 * 通过名字获取上下文中的bean
	 * 
	 * @param name
	 * @return
	 */
	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}

	/**
	 * 通过类型获取上下文中的bean
	 * 
	 * @param requiredType
	 * @return
	 */
	public static Object getBean(Class<?> requiredType) {
		return applicationContext.getBean(requiredType);
	}

	/**
	 * 获取application 里的属性值
	 * @param key
	 * @return
	 */
	public static String getAttributeValue(String key){
		try {
			return  applicationContext.getEnvironment().getProperty(key);
		} catch (Exception e) {
		    e.printStackTrace();
		    return  null;
		}

	}

	public static void println() {
		println("***************");
		printBase();
	}

	public static void printlnInfo() {
		println("***************");
		Object dataSource = getBean("dataSource");
		if (dataSource != null) {
			println(">>>>数据源>>>>>" + dataSource.getClass());
		} else {
			println(">>>>数据源>>>>>: 不存在,有异常");
		}
		printBase();
	}

	private static void printBase() {
		Environment environment = getApplicationContext().getEnvironment();
		String project_url = "http://localhost:" + environment.getProperty("server.port") + environment.getProperty("server.context-path");
		println(project_url);
		String profile = environment.getProperty("spring.profiles.active");
		if (StringUtil.isNotEmpty(profile)) {
			println("当前激活的环境文件:" + profile);
		}
		println("当前项目资源路径:SpringContextUtil.getCurrentPath()="+getCurrentPath());
		println("***************");
	}

	private static void println(Object obj){
		log.info(obj.toString());
	}

}