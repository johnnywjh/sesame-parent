package kim.sesame.common.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户实体
 */
@Setter
@Getter
public class IUser implements IEntity {

    private String id;
    private String account;//账号,唯一
    private String name;//昵称
    private String pwd;//密码
    private String pwdVersion;// 密码版本

    @Override
    public String toString() {
        return "IUser{" +
                "id='" + getId() + '\'' +
                ", account='" + getAccount() + '\'' +
                ", name='" + getName() + '\'' +
                ", pwdVersion='" + getPwdVersion() + '\'' +
                '}';
    }

    public String getUserAccountAndName() {
        return getUserAccountAndName(" ");
    }

    public String getUserAccountAndName(String s) {
        return getAccount() + s + getName();
    }
}