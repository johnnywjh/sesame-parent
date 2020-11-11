package kim.sesame.framework.serial.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 系统默认属性
 *
 * @author johnny
 * date :  2017年9月7日 下午7:20:50
 * server.servlet.session.timeout=30
 */
@Data
@Component
@ConfigurationProperties(prefix = "sesame.framework.serial")
public class SerrialProperties {

    /**
     * 序列化表的表名 默认 t_bse_serial_number_rule
     */
    private String tableName = "t_bse_serial_number_rule";

}
