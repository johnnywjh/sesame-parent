package com.sesame.framework.serial.entity;

import com.sesame.framework.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 序列号实体
 **/
@Data
public class SerialNumberRuleEntity extends BaseEntity {
    /**编码*/
    private String code;
    /**名称*/
    private String name;
    /**当前日期*/
    private Date curTime;
    /**当前值*/
    private Long curNum;
}
