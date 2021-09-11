package kim.sesame.framework.serial.entity;

import lombok.Data;

import java.util.Date;

/**
 * 序列号实体
 **/
@Data
public class SerialNumberRuleEntity {
    private String id;
    /**编码*/
    private String code;
    /**名称*/
    private String name;
    /**当前日期*/
    private Date curTime;
    /**当前值*/
    private Long curNum;
}
