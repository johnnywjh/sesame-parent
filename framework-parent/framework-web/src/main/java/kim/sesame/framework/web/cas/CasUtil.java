package kim.sesame.framework.web.cas;

import kim.sesame.framework.web.context.SpringContextUtil;
import kim.sesame.framework.web.util.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CasUtil {

    /**
     * 将 sessionid 保存到 cookie 中
     *
     * @param request
     * @param response
     */
    public static String saveSessionIdToCookie(HttpServletRequest request, HttpServletResponse response) {
        String sessionId = request.getSession().getId();
        return saveSessionIdToCookie(sessionId, response);
    }

    public static String saveSessionIdToCookie(String sessionId, HttpServletResponse response) {
        CasProperties cas = SpringContextUtil.getApplicationContext().getBean(CasProperties.class);
        if (cas != null) {
            CookieUtil.addCookie(response, cas.getSessionIdKey(), sessionId, cas.getExpirationTime());
            return sessionId;
        }
        return null;
    }

    /**
     * 获取 保存到 cookie 中的 sessionid
     *
     * @param request
     * @return
     */
    public static String getSessionId(HttpServletRequest request) {
        CasProperties cas = SpringContextUtil.getApplicationContext().getBean(CasProperties.class);
        if (cas != null) {
            String sessionId = CookieUtil.getCookieByNameToString(request, cas.getSessionIdKey());
            return sessionId;
        }
        return null;
    }
}
