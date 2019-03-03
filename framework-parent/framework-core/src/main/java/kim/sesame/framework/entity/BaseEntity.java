package kim.sesame.framework.entity;

import kim.sesame.framework.utils.UUIDUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体的父类,主要给 mybatis 使用
 */
@Data
public class BaseEntity implements Serializable {

    /**
     * 主键
     */
    private String id;
    /**
     * 创建人
     */
    private String createUser;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改人
     */
    private String updateUser;
    /**
     * 修改时间
     */
    private Date updateTime;

    public void initCreateAndId(String user) {
        initCreateAndId(user, UUIDUtil.getShortUUID());
    }

    public void initCreateAndId(String user, String id) {
        setId(id);
        initCreate(user);
    }

    public void initCreate(String user) {
        Date d = new Date();
        setCreateUser(user);
        setCreateTime(d);
        setUpdateUser(user);
        setUpdateTime(d);
    }


    public void initUpdate(String user) {
        Date d = new Date();
        setUpdateUser(user);
        setUpdateTime(d);
    }

}
