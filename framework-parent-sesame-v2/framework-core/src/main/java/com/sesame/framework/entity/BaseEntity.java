package com.sesame.framework.entity;

import com.sesame.framework.utils.UUIDUtil;
import lombok.Data;

import java.util.Date;

/**
 * 基础实体
 *
 * @author wangjianghai
 * @date 2017/10/26 15:20
 */
@Data
public class BaseEntity implements IEntity {

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
        this.id = id;
        initCreate(user);
    }

    public void initCreate(String user) {
        Date d = new Date();
        this.createUser = user;
        this.createTime = d;
        this.updateUser = user;
        this.updateTime = d;
    }

    public void initUpdate(String user) {
        Date d = new Date();
        this.updateUser = user;
        this.updateTime = d;
    }

}
