package kim.sesame.framework.web.entity;

import kim.sesame.framework.entity.BaseEntity;
import lombok.Data;

/**
 * 用户实体
 */
@Data
public class IUser extends BaseEntity {

    private String account;//账号,唯一
    private String name;//昵称
    private String pwd;//密码

    @Override
    public String toString() {
        return "IUser{" +
                "id='" + super.getId() + '\'' +
                "account='" + account + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getUserAccountAndName() {
        return getUserAccountAndName(" ");
    }

    public String getUserAccountAndName(String s) {
        return getAccount() + s + getName();
    }
}