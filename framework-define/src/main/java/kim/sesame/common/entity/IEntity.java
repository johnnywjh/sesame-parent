package kim.sesame.common.entity;

import kim.sesame.common.utils.UUIDUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体的接口,主要给 mybatis-plus 使用
 */
public interface IEntity extends Serializable {
    String getId();

    void setId(String id);

    default String getCreateUser() {
        return null;
    }

    default void setCreateUser(String createUser) {
    }

    default Date getCreateTime() {
        return null;
    }

    default void setCreateTime(Date createTime) {
    }

    default String getModifyUser() {
        return null;
    }

    default void setModifyUser(String updateUser) {
    }

    default Date getModifyTime() {
        return null;
    }

    default void setModifyTime(Date updateTime) {
    }


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
        setModifyUser(user);
        setModifyTime(d);
    }

    default void initModify(String user) {
        Date d = new Date();
        setModifyUser(user);
        setModifyTime(d);
    }

}
