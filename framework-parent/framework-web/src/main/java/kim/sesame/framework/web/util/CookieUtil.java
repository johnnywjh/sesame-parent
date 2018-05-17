package kim.sesame.framework.web.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Cookie 的封装
 *
 * @author johnny
 * date :  2017年5月12日 上午12:15:01
 * Description:
 */
public class CookieUtil {

    /**
     * 设置cookie
     *
     * @param response response
     * @param name     cookie名字
     * @param value    cookie值
     * @param maxAge   cookie生命周期 以秒为单位
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        try {
            Cookie cookie = new Cookie(name, value);
            cookie.setPath("/");
            if (maxAge > 0) {
                cookie.setMaxAge(maxAge);
            }
            response.addCookie(cookie);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据名字获取cookie
     *
     * @param request request
     * @param name    cookie名字
     * @return Cookie
     */
    public static Cookie getCookieByName(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = ReadCookieMap(request);
        if (cookieMap.containsKey(name)) {
            Cookie cookie = (Cookie) cookieMap.get(name);
            return cookie;
        } else {
            return null;
        }
    }
    public static String getCookieByNameToString(HttpServletRequest request, String name) {
        try {
            Map<String, Cookie> cookieMap = ReadCookieMap(request);
            if (cookieMap.containsKey(name)) {
                Cookie cookie = (Cookie) cookieMap.get(name);
                return cookie.getValue();
            }else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将cookie封装到Map里面
     *
     * @param request request
     * @return Map<String, Cookie>
     */
    private static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }
}
