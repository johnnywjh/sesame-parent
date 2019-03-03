package kim.sesame.framework.entity;

import kim.sesame.framework.utils.UUIDUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体的接口,主要给 mybatis-plus 使用
 */
public interface IEntity extends Serializable {
    String getId();
    void setId(String id);

    default String getCreateUser(){return null;}
    default void setCreateUser(String createUser){}

    default Date getCreateTime(){return null;}
    default void setCreateTime(Date createTime){}

    default String getUpdateUser(){return null;}
    default void setUpdateUser(String updateUser){}

    default Date getUpdateTime(){return null;}
    default void setUpdateTime(Date updateTime){}


     default void initCreateAndId(String user) {
         initCreateAndId(user, UUIDUtil.getShortUUID());
     }

     default void initCreateAndId(String user, String id) {
         setId(id);
         initCreate(user);
     }

     default void initCreate(String user) {
         Date d = new Date();
         setCreateUser(user);
         setCreateTime(d);
         setUpdateUser(user);
         setUpdateTime(d);
     }


     default void initUpdate(String user) {
         Date d = new Date();
         setUpdateUser(user);
         setUpdateTime(d);
     }

}
