package kim.sesame.framework.util.accessory;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 自动扫描
 */
@Configuration
@ComponentScan
@MapperScan(value = {"kim.sesame.framework.util.accessory.dao"})
public class AccessoryConfiguration {

}
