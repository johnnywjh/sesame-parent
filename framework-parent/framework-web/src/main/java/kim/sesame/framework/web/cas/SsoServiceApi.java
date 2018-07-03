package kim.sesame.framework.web.cas;

/**
 * 接口地址配置
 */
public class SsoServiceApi {
    /**
     * sso 服务器登录接口
     */
    public static final String login = "/p/login";
    /**
     * sso 服务器判断是否登录,并返回 sessionId
     */
    public static final String verifyLogin = "/user/verifyLogin";
    /**
     * sso 保存登录信息
     */
    public static final String saveLogin = "/user/saveLogin";

}
