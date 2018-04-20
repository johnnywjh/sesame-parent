package kim.sesame.framework.web.cas;

/**
 * 接口地址配置
 */
public class CasServiceApi {
    /**
     * cas 服务器登录接口
     */
    public static final String login = "/page/login";
    /**
     *cas 服务器判断是否登录,并返回 sessionId
     */
    public static final String verifyLogin = "/user/verifyLogin";

    /**
     *cas 保存登录信息
     */
    public static final String saveLogin = "/user/saveCasLogin";

}
