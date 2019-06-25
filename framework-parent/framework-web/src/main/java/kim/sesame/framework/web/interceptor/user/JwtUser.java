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
    private String userAccount;
    private String pwdVersion;

}
