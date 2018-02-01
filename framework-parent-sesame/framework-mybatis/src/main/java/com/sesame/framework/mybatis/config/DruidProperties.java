package com.sesame.framework.mybatis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sesame.framework.mybatis")
public class DruidProperties {

	private String mapperPath = "classpath*:com/sesame/**/dao/*.xml";
	private boolean druidEnabled = false;
	private String druidLoginName = "admin";
	private String druidLoginPwd = "123456";

}
