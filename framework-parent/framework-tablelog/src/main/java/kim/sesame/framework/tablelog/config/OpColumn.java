package kim.sesame.framework.tablelog.config;

import lombok.Data;

@Data
public class OpColumn {
    /**
     * 字段名称
     */
    private String columnName;
    /**
     * 字段注释
     */
    private String comment;
    /**
     * 对应的java属性
     */
    private String javaName;
    /**
     * 查询sql
     */
    private String selectSql;
    /**
     * 当字段类型为String 时, 对于 ""  是否做记录
     */
    private boolean strNull = false;

}
