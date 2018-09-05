package kim.sesame.framework.tablelog.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "sesame.framework.tablelog")
public class TableOpProperties implements InitializingBean {

    /**
     * 是否开启, 默认关闭
     */
    private boolean enable = false;
    /**
     * 用表名 做配置
     */
    private List<OpTable> list;


    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
