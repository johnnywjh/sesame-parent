package kim.sesame.framework.web.entity;

import kim.sesame.framework.entity.IEntity;
import lombok.Data;

/**
 * 用户实体
 */
@Data
public class IUser implements IEntity {

    private String id;
    private String account;//账号,唯一
    private String name;//昵称
    private String pwd;//密码

    @Override
    public String toString() {
        return "IUser{" +
                "id='" + getId() + '\'' +
                "account='" + getAccount() + '\'' +
                ", name='" + getName() + '\'' +
                '}';
    }

    public String getUserAccountAndName() {
        return getUserAccountAndName(" ");
    }

    public String getUserAccountAndName(String s) {
        return getAccount() + s + getName();
    }
}