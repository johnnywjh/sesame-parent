package com.sesame.framework.serial.dao;


import com.sesame.framework.serial.entity.SerialNumberRuleEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 序列号Dao
 **/
@Mapper
public interface SerialNumberRuleDao {

    /* 检查表 如果不存在就创建 */
    void check_notExistsCreate();

    /* 根据code查询序号实体并添加悲观锁 */
    SerialNumberRuleEntity querySerialNumberRuleForUpdate(String code);

    /* 新增序号信息*/
    void addSerialNumberRule(SerialNumberRuleEntity serialNumberRuleEntity);

    /* 更新序号信息*/
    void updateSerialNumberRule(SerialNumberRuleEntity serialNumberRuleEntity);
}
