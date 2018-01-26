package com.sesame.framework.web.interceptor.user;

import com.sesame.framework.utils.GData;
import com.sesame.framework.utils.StringUtil;
import com.sesame.framework.web.cache.IUserCache;
import com.sesame.framework.web.context.SpringContextUtil;
import com.sesame.framework.web.context.UserContext;
import com.sesame.framework.web.entity.IUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户信息拦截和设置
 */
public class UserInfoInterceptor extends HandlerInterceptorAdapter {

    /**
     * 一个请求的第一个拦截方法,给这个线程添加数据
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 用户进来只有两种方式 1 sessionid , 2 userNo

        //   web 方式进入
        String sessionId = "";
        String zuulSessionId = request.getParameter(GData.CLOUD.ZUUL_SESSION_ID);
        if(StringUtil.isEmpty(zuulSessionId)){
            sessionId = request.getSession().getId();
        }else{
            sessionId = zuulSessionId;
        }
        UserContext.getUserContext().setUserSessionId(sessionId);

        IUserCache userCache = (IUserCache) SpringContextUtil.getBean(IUserCache.USER_LOGIN_BEAN);
        String userNo = userCache.getUserNo(sessionId); // 用户账号

        IUser user = null;
        List list_roles = null;
        if(StringUtil.isNotEmpty(userNo)){
            user = userCache.getUserCache(userNo);
            list_roles = userCache.getUserRoles(userNo);
        }

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


}