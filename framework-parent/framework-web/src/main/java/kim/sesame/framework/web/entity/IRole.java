package kim.sesame.framework.web.entity;


import kim.sesame.framework.entity.BaseEntity;
import kim.sesame.framework.entity.IEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 角色实体接口
 */
@Data
public class IRole extends BaseEntity {

    private String code;//角色编号 唯一
    private String name;//角色名称
    private Integer grade;//角色等级
    private String remark;//备注
    private String notdele;//是否允许删除 : Y/N
    private String active;//是否有效 : Y/N

    // not database field ...
    private String roles; // 角色集合
    private int userCount;// 用户个数

    @Override
    public String toString() {
        return "IRole{" +
                "id='" + super.getId() + '\'' +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", grade=" + grade +
                '}';
    }
}