package kim.sesame.framework.web.interceptor.user;

import io.jsonwebtoken.Claims;
import kim.sesame.framework.utils.GData;
import kim.sesame.framework.utils.StringUtil;
import kim.sesame.framework.web.annotation.IgnoreLoginCheck;
import kim.sesame.framework.web.cache.IUserCache;
import kim.sesame.framework.web.cas.SsoUtil;
import kim.sesame.framework.web.context.SpringContextUtil;
import kim.sesame.framework.web.context.UserContext;
import kim.sesame.framework.web.entity.IRole;
import kim.sesame.framework.web.entity.IUser;
import kim.sesame.framework.web.jwt.JwtHelper;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户信息拦截和设置
 */
@CommonsLog
public class UserInfoInterceptor extends HandlerInterceptorAdapter {

    /**
     * 一个请求的第一个拦截方法,给这个线程添加数据
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //过滤掉静态资源
        if (handler instanceof org.springframework.web.servlet.resource.ResourceHttpRequestHandler) {
            return true;
        }

        String requestUrl = request.getRequestURL().toString();
        log.debug(">>>>>>1 requestUrl : " + requestUrl);

        String sessionId = getSessionId(request);
        UserContext.getUserContext().setUserSessionId(sessionId);

        // 1.方法名称上有忽略注解的==>直接放行
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            if (method.getMethod().isAnnotationPresent(IgnoreLoginCheck.class)) {
                if (!method.getMethod().getAnnotation(IgnoreLoginCheck.class).isLoadUser()) {
                    return true;
                }
            }
        }
        String userNo = null;
//        IUserCache userCache = (IUserCache) SpringContextUtil.getBean(IUserCache.USER_LOGIN_BEAN);
        IUserCache userCache = SpringContextUtil.getBean(IUserCache.class);
        userNo = userCache.getUserNo(sessionId); // 用户账号

        IUser user = null;
        List<IRole> list_roles = null;
        if (StringUtil.isNotEmpty(userNo)) {
            user = userCache.getUserCache(userNo);
            list_roles = userCache.getUserRoles(userNo);
        }
        log.debug(">>>>>>3 userNo : " + userNo);
        log.debug(">>>>>>4 user : " + user);

        UserContext.getUserContext().setUser(user);
        UserContext.getUserContext().setUserRole(list_roles);

        return true;
    }

    /**
     * 一个请求的最后一个拦截方法
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.getUserContext().clean();
    }

    /**
     * 根据用户进入的方式,获取sessionId,表示用户表示,没有实际意义
     */
    private String getSessionId(HttpServletRequest request) {

        String sessionId = null;

        String token = getToken(request);
        if (StringUtil.isNotEmpty(token)) {
            Claims claims = JwtHelper.parseJWT(token);
            if (claims != null) {
                sessionId = getClaimsKey(claims, GData.JWT.SESSION_ID);
                if (StringUtil.isNotEmpty(sessionId)) {
                    log.debug(">>>>>>2 token_session : " + sessionId);
                    return sessionId;
                }
            }
        }
        sessionId = request.getParameter("sessionId");
        if (StringUtil.isNotEmpty(sessionId)) {
            log.debug(">>>>>>2 sessionId : " + sessionId);
            return sessionId;
        }

        String casSessionId = SsoUtil.getSessionId(request);
        if (StringUtil.isNotEmpty(casSessionId)) {
            sessionId = casSessionId;
            log.debug(">>>>>>2 casSessionId : " + sessionId);
        } else {
            sessionId = request.getSession().getId();
            log.debug(">>>>>>2 requestSessionId : " + sessionId);
        }
        return sessionId;
    }

    /**
     * 从请求中获取 token,
     * 当请求都里没有的时候 从请求参数里去获取
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(GData.JWT.TOKEN);
        if (StringUtil.isEmpty(token)) {
            token = request.getParameter(GData.JWT.TOKEN);
            if (StringUtil.isEmpty(token)) {
                log.debug("获取请求体(body)里的" + GData.JWT.TOKEN + ":" + token);
            }
        } else {
            log.debug("获取请求头(head)里的" + GData.JWT.TOKEN + ":" + token);
        }
        return token;
    }


    private String getClaimsKey(Claims claims, String key) {
        try {
            return claims.get(key).toString();
        } catch (Exception e) {
            return null;
        }
    }
}