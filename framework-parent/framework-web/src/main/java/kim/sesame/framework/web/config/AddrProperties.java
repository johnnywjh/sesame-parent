package kim.sesame.framework.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 开发,测试,生成,构建. 服务器的地址
 *
 * @author johnny
 * date :  2017年9月7日 下午7:20:50
 * Description:
 */
@Data
@Component
@ConfigurationProperties(prefix = "addr")
public class AddrProperties {

    private String dev;
    private String test;
    private String prod;
    private String gj;

}
