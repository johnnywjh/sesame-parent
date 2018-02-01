package kim.sesame.framework.mybatis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * mybatis 属性配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "sesame.framework.mybatis")
public class DruidProperties {

	/**
	 *  dao.xml 扫描规则配置,尽量不要修改,因为架构里有的模块有公用 dao.xml
	 */
	private String mapperPath ="classpath*:**/dao/*DaoMapping.xml"; // "classpath*:kim/sesame/**/dao/*.xml";

	private boolean druidEnabled = false;
	private String druidLoginName = "admin";
	private String druidLoginPwd = "123456";

}
