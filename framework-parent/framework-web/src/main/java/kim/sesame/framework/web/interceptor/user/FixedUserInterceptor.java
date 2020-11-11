package kim.sesame.framework.web.interceptor.user;

import kim.sesame.framework.web.annotation.IgnoreLoginCheck;
import kim.sesame.framework.web.cache.IUserCache;
import kim.sesame.framework.web.config.WebProperties;
import kim.sesame.framework.web.context.SpringContextUtil;
import kim.sesame.framework.web.context.UserContext;
import kim.sesame.framework.web.entity.IUser;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 固定用户登录
 */
@CommonsLog
public class FixedUserInterceptor extends HandlerInterceptorAdapter {

    /**
     * 一个请求的第一个拦截方法,给这个线程添加数据
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //过滤掉静态资源
        if (handler instanceof org.springframework.web.servlet.resource.ResourceHttpRequestHandler) {
            return true;
        }

        // 1.方法名称上有忽略注解的==>直接放行
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            if (method.getMethod().isAnnotationPresent(IgnoreLoginCheck.class)) {
                if (!method.getMethod().getAnnotation(IgnoreLoginCheck.class).isLoadUser()) {
                    return true;
                }
            }
        }

        WebProperties web = SpringContextUtil.getBean(WebProperties.class);

        if(web.isFixedUserEnable()){
            String userNo = web.getFixedUserAccount();
            IUserCache userCache = SpringContextUtil.getBean(IUserCache.class);
            UserContext.getUserContext().setCurrentLoginUserAccount(userNo);
            IUser user = userCache.getUserCache(userNo);

            log.debug(">>>>>> 登录的用户账号 : " + userNo);
            log.debug(">>>>>> 登录的用户实体 : " + user);

            UserContext.getUserContext().setUser(user);
        }

        return true;
    }

    /**
     * 一个请求的最后一个拦截方法
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.getUserContext().clean();
    }

}