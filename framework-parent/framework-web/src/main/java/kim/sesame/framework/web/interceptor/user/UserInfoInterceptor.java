package kim.sesame.framework.web.interceptor.user;

import io.jsonwebtoken.Claims;
import kim.sesame.framework.utils.GData;
import kim.sesame.framework.utils.StringUtil;
import kim.sesame.framework.web.annotation.IgnoreLoginCheck;
import kim.sesame.framework.web.cache.IUserCache;
import kim.sesame.framework.web.cas.CasUtil;
import kim.sesame.framework.web.context.SpringContextUtil;
import kim.sesame.framework.web.context.UserContext;
import kim.sesame.framework.web.entity.IRole;
import kim.sesame.framework.web.entity.IUser;
import kim.sesame.framework.web.jwt.JwtHelper;
import lombok.Data;
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

        ReqUser reqUser = getReqUser(request);
        UserContext.getUserContext().setUserSessionId(reqUser.getSessionId());

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
        IUserCache userCache = (IUserCache) SpringContextUtil.getBean(IUserCache.USER_LOGIN_BEAN);
        if(StringUtil.isNotEmpty(reqUser.getAccount())){
            userNo = reqUser.getAccount();
        }else{
            userNo = userCache.getUserNo(reqUser.getSessionId()); // 用户账号
        }

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
     * 根据用户进入的方式,获取sessionId, 或是 account
     */
    public ReqUser getReqUser(HttpServletRequest request) {
        ReqUser bean = null;
        String sessionId = null;
        String account = null;
        String token = request.getParameter(GData.JWT.TOKEN);
        if (StringUtil.isNotEmpty(token)) {
            Claims claims = JwtHelper.parseJWT(token);
            if (claims != null) {
                account = claims.get(GData.JWT.USER_ACC).toString();
                sessionId = claims.get(GData.JWT.SESSION_ID).toString();
                if (account != null || sessionId != null) {
                    bean = new ReqUser(sessionId, account);
                }
            }
        }
        if (bean != null) {
            return bean;
        }

        String casSessionId = CasUtil.getSessionId(request);
        if (StringUtil.isNotEmpty(casSessionId)) {
            sessionId = casSessionId;
            log.debug(">>>>>>2 casSessionId : " + sessionId);
        } else {
            String zuulSessionId = request.getParameter(GData.CLOUD.ZUUL_SESSION_ID);
            if (StringUtil.isNotEmpty(zuulSessionId)) {
                sessionId = zuulSessionId;
                log.debug(">>>>>>2 zuulSessionId : " + sessionId);
            } else {
                sessionId = request.getSession().getId();
                log.debug(">>>>>>2 requestSessionId : " + sessionId);
            }
        }
        return new ReqUser(sessionId, null);
    }

    public String getToken(HttpServletRequest request){
        String token = request.getParameter(GData.JWT.TOKEN);
        if(StringUtil.isNotEmpty(token)){
            return  token;
        }
//        request.getHeader()
        return  token;
    }

    @Data
    class ReqUser {
        private String sessionId;
        private String account;

        public ReqUser() {
        }

        public ReqUser(String sessionId, String account) {
            this.sessionId = sessionId;
            this.account = account;
        }
    }
}