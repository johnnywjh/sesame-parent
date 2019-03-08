package kim.sesame.framework.web.entity;


import kim.sesame.framework.entity.BaseEntity;
import lombok.Data;

/**
 * 角色实体接口
 */
@Data
public class IRole extends BaseEntity {

    private String code;//角色编号 唯一
    private String name;//角色名称
    private Integer grade;//角色等级

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