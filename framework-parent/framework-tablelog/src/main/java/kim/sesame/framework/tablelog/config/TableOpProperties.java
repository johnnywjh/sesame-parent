package kim.sesame.framework.tablelog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "sesame.framework.tablelog")
//@PropertySource(value="classpath:config/application.properties",encoding = "UTF-8")
public class TableOpProperties {

    /**
     * 是否开启, 默认关闭
     */
    private boolean enable = false;
    /**
     * 用表名 做配置
     */
    private List<OpTable> list;


    /**
     * 根据类路径获取 OpTable 对象
     */
    public OpTable getOpTable(String classPath) {
        OpTable obj = null;
        for (OpTable o : this.list) {
            if (o.getClassPath().equals(classPath)) {
                obj = o;
                break;
            }
        }
        return obj;
    }

}
