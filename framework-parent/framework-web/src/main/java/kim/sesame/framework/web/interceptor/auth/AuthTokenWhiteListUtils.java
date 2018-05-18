package kim.sesame.framework.web.interceptor.auth;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * token认证的白名单工具类，白名单匹配成功的编号可以不用动态口令的校验，反之则需要校验动态口令
 *
 * @author johnny
 * date :  2016年9月28日 上午8:42:08  
 */
public class AuthTokenWhiteListUtils {
    private static Set<String> whiteList = null;

    public static void setWhiteList(Set<String> whiteList) {
        AuthTokenWhiteListUtils.whiteList = whiteList;
    }

    /**
     * Description:验证白名单内容
     *
     * @param srcIp srcip
     * @return boolean  返回类型
     */
    public static boolean isValid(String srcIp) {
        if (!whiteList.contains(srcIp)) {
            for (String whIp : whiteList) {
                if (whIp.contains("*")) {
                    Pattern p = Pattern.compile(whIp.replace("*", "\\w*"));
                    Matcher m = p.matcher(srcIp);
                    return m.matches();
                }
            }
            return false;
        }
        return true;
    }

}
