package com.sesame.framework.mybatis.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * BaseDao
 *
 * @author johnny
 * @date 2017-11-29 13:05
 * @Description: 基础Dao
 */
@Mapper
public interface BaseDao {

    /**
     * 判断表是否存在
     * @param tableName 表名
     * @return
     */
    String checkTableExists(String tableName);


}