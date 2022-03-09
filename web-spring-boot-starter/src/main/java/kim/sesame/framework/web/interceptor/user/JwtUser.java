package kim.sesame.framework.web.interceptor.user;

import lombok.Data;

/**
 * jwt里的用户数据
 */
@Data
public class JwtUser {
    private String sessionId;

    /**
     * 直接用账号加载用户信息,userAccount和pwdVersion必须不为空
     */
    private boolean accLoad = false;
    private String account;
    private String name;
    private String pwdVersion;
    /**
     * 是否边角密码版本号, 默认不比较
     */
    private boolean comparePwdVersion = false;

    @Override
    public String toString() {
        return "JwtUser{" +
                "sessionId='" + sessionId + '\'' +
                ", accLoad=" + accLoad +
                ", account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", comparePwdVersion='" + comparePwdVersion + '\'' +
                ", pwdVersion='" + pwdVersion + '\'' +
                '}';
    }
}
