package kim.sesame.framework.web.interceptor.auth;

import kim.sesame.framework.utils.StringUtil;
import kim.sesame.framework.web.context.SpringContextUtil;
import kim.sesame.framework.web.util.FileUtil;
import lombok.extern.apachecommons.CommonsLog;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.text.MessageFormat;

/**
 * 根据对应的公钥和密钥规则创建相应的动态口令，对比相应的动态口令
 *
 * @author johnny
 * date :  2016年9月27日 上午10:43:34  
 */
@CommonsLog
public class AuthTokenUtils {
    /* 公钥   */
    public static final String PUBLIC_TOKEN_KEY = "PUBLIC_TOKEN";
    /*  动态tokean  */
    public static final String DYNAMIC_TOKEN_KEY = "DYNAMIC_TOKEN";

    // JavaScript容器引擎管理器
    private static ScriptEngine scriptEngine = null;
    private static Invocable inv = null;

    static {
        try {
            ScriptEngineManager sem = new ScriptEngineManager();
            scriptEngine = sem.getEngineByName("js");
            scriptEngine.eval(FileUtil.inputStream2String(AuthTokenUtils.class.getResourceAsStream("AuthToken.js")));
            inv = (Invocable) AuthTokenUtils.scriptEngine;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取动态口令值 token : 口令，c:误差时效 单位 分钟
     *
     * @param token         token
     * @param private_token private_token
     * @param c             c
     * @return String    返回类型 
     * @throws Exception js执行异常 
     */
    public static String getAuthToken_s(String token, String private_token, int c) throws Exception {
        String res = (String) inv.invokeFunction("getAuthToken_s", token, private_token, c);
        return res;
    }

    /**
     * 对比动态口令是否正确，
     *
     * @param publicToken 公钥
     * @param authToken   根据公钥,私钥算出来的值
     * @return 如果动态口令正确则返回true，不正确则返回false
     * @throws Exception
     */
    public static boolean isValid(String publicToken, String authToken) throws Exception {
        if (StringUtil.isEmpty(publicToken)) {
            return false;
        }
        // 1.根据公钥获取私钥对象
        AuthProperties auth = SpringContextUtil.getBean(AuthProperties.class);
        AuthProperties.AuthToken tokean = auth.getAuthToken().get(publicToken);
        if (tokean == null) {
            return false;
        }
        // 如果配置关闭了,那么不允许访问
        if(!tokean.isEnable()){
            return false;
        }

        // 获取私钥
        String privateToken = tokean.getPrivateToken();
        if (privateToken != null) {
            // 2.通过公钥私钥和时间有效数获取动态authToken
            String localAuthToken = AuthTokenUtils.getAuthToken_s(publicToken, privateToken, tokean.getTimeOut());
            // 3.对比动态客户端提交的authToken和服务器authToken是否一致，如果一致返回true，不一致返回false
            if (localAuthToken.equals(authToken)) {
                log.debug(MessageFormat.format("token : {0} ,认证成功,{1}", publicToken, tokean.getRemark()));
                return true;
            }
        }
        return false;
    }
}
