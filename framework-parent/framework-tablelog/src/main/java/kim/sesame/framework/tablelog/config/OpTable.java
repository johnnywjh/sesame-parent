package kim.sesame.framework.tablelog.config;

import lombok.Data;

import java.util.List;

@Data
public class OpTable {
    /**
     * 表名称
     */
    private String tableName;
    /**
     * 实体类路径
     */
    private String classPath;
    /**
     * 主键字段, 默认值 id
     */
    private String pkId = "id";
    /**
     * 对应的java属性, 默认值 id
     */
    private String pkJava = "id";

    /**
     * 是否同步,记录日志, 默认false
     */
    private boolean sync=false;

    /**
     * 列的集合
     */
    private List<OpColumn> columnList;

}
