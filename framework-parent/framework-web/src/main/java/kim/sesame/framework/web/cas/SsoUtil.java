package kim.sesame.framework.web.cas;

import kim.sesame.framework.web.config.WebProperties;
import kim.sesame.framework.web.context.SpringContextUtil;
import kim.sesame.framework.web.util.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SsoUtil {

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
        SsoProperties sso = SpringContextUtil.getBean(SsoProperties.class);
        WebProperties web = SpringContextUtil.getBean(WebProperties.class);
        if (sso != null) {
            CookieUtil.addCookie(response, sso.getSessionIdKey(), sessionId, web.getUserLoginSaveTime() * 60);
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
        SsoProperties sso = SpringContextUtil.getBean(SsoProperties.class);
        if (sso != null) {
            String sessionId = CookieUtil.getCookieByNameToString(request, sso.getSessionIdKey());
            return sessionId;
        }
        return null;
    }
}
