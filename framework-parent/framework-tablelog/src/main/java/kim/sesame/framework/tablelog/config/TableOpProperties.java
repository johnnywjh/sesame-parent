package kim.sesame.framework.tablelog.config;

import kim.sesame.framework.utils.StringUtil;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "sesame.framework.tablelog")
//@PropertySource(value="classpath:config/application.properties",encoding = "UTF-8")
public class TableOpProperties implements InitializingBean {

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

    @Override
    public void afterPropertiesSet() throws Exception {
        // 属性加载完成之后的操作

        // 如果java属性和 数据库字段一样, 可以只写数据库字段
        if (this.enable == false) {
            return;
        }
        if (this.list == null || this.list.size() == 0) {
            return;
        }
        for (OpTable ot : this.list) {
            List<OpColumn> columnList = ot.getColumnList();
            if (columnList == null || columnList.size() == 0) {
                continue;
            }
            for (OpColumn c : columnList) {
                boolean flg_db = StringUtil.isNotEmpty(c.getColumnName());
                boolean flg_java = StringUtil.isNotEmpty(c.getJavaName());
                if (flg_db && flg_java) {
                    continue;
                }
                if (flg_db == false && flg_java == false) {
                    throw new NullPointerException("请正确配置java属性和数据库字段属性");
                }
                if (flg_db) {
                    c.setJavaName(c.getColumnName());
                } else {
                    c.setColumnName(c.getJavaName());
                }
            }
        }
    }
}
