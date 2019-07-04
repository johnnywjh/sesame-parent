package kim.sesame.framework.web.interceptor.user;

import io.jsonwebtoken.Claims;
import kim.sesame.framework.utils.GData;
import kim.sesame.framework.utils.StringUtil;
import kim.sesame.framework.web.annotation.IgnoreLoginCheck;
import kim.sesame.framework.web.cache.IUserCache;
import kim.sesame.framework.web.cas.SsoUtil;
import kim.sesame.framework.web.context.SpringContextUtil;
import kim.sesame.framework.web.context.UserContext;
import kim.sesame.framework.web.entity.IUser;
import kim.sesame.framework.web.jwt.JwtHelper;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

        JwtUser jwtUser = parentReqData(request);
        log.debug("解析用户认证数据:" + jwtUser);
        String sessionId = jwtUser.getSessionId();
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
        IUserCache userCache = SpringContextUtil.getBean(IUserCache.class);

        if (jwtUser.isAccLoad()) {
            userNo = jwtUser.getUserAccount();
        } else {
            userNo = userCache.getUserNo(sessionId); // 用户账号
        }
        UserContext.getUserContext().setCurrentLoginUserAccount(userNo);

        IUser user = null;
        if (StringUtil.isNotEmpty(userNo)) {
            user = userCache.getUserCache(userNo);

            // 如果jwt传入的是 用户账号, 需要校验密码版本
            // 修改密码后记得要清理用户缓存,不然密码版本一直会校验失败
            if (jwtUser.isAccLoad() && user != null) {
                if (!jwtUser.getPwdVersion().equals(user.getPwdVersion())) {
                    user = null;
                }
            }
        }
        log.debug(">>>>>>3 userNo : " + userNo);
        log.debug(">>>>>>4 user : " + user);

        UserContext.getUserContext().setUser(user);

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
     * 解析用户请求数据
     */
    private JwtUser parentReqData(HttpServletRequest request) {

        JwtUser jwtUser = new JwtUser();
        String sessionId = null;

        String token = getToken(request);
        if (StringUtil.isNotEmpty(token)) {
            Claims claims = JwtHelper.parseJWT(token);
            if (claims != null) {

                // 获取jwt中当前登录的用户信息
                Object userAccount = getClaimsKey(claims, GData.JWT.USER_ACCOUNT);
                jwtUser.setUserAccount(userAccount != null ? userAccount.toString() : null);

                Object pwdVersion = getClaimsKey(claims, GData.JWT.PWD_VERSION);
                jwtUser.setPwdVersion(pwdVersion != null ? pwdVersion.toString() : null);

                Object accLoad = getClaimsKey(claims, GData.JWT.ACC_LOAD);
                if (accLoad != null) {
                    jwtUser.setAccLoad((Boolean) accLoad);
                }

                Object sessionObj = getClaimsKey(claims, GData.JWT.SESSION_ID);
                if (sessionObj != null) {
                    sessionId = sessionObj.toString();
                }
            }
        } else {
            sessionId = request.getParameter("sessionId");
            if (StringUtil.isEmpty(sessionId)) {
                String casSessionId = SsoUtil.getSessionId(request);
                if (StringUtil.isNotEmpty(casSessionId)) {
                    sessionId = casSessionId;
                }
            }
        }

        if (sessionId == null || sessionId == "") {
            sessionId = request.getSession().getId();
        }
        jwtUser.setSessionId(sessionId);
        return jwtUser;
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


    private Object getClaimsKey(Claims claims, String key) {
        try {
            return claims.get(key);
        } catch (Exception e) {
            return null;
        }
    }
}