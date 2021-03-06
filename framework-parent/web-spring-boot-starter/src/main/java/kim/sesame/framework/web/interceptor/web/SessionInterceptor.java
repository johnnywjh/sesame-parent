package kim.sesame.framework.web.interceptor.web;

import com.alibaba.fastjson.JSON;
import kim.sesame.framework.entity.IUser;
import kim.sesame.framework.utils.GData;
import kim.sesame.framework.web.annotation.IgnoreLoginCheck;
import kim.sesame.framework.web.response.ResponseFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 登录信息保存在session中的拦截
 */
public class SessionInterceptor extends HandlerInterceptorAdapter {

    /**
     * 在业务处理器处理请求之前被调用 如果返回false 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
     * 如果返回true 执行下一个拦截器,直到所有的拦截器都执行完毕 再执行被拦截的Controller 然后进入拦截器链,
     * 从最后一个拦截器往回执行所有的postHandle() 接着再从最后一个拦截器往回执行所有的afterCompletion()
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //过滤掉静态资源
        if (handler instanceof org.springframework.web.servlet.resource.ResourceHttpRequestHandler) {
            return true;
        }

        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            // 1.方法名称上有忽略注解的==>直接放行
            if (method.getMethod().isAnnotationPresent(IgnoreLoginCheck.class)) {
                return true;
            }
        }

        // 2 用户已登录
        IUser user = (IUser) request.getSession().getAttribute(GData.SESSION.LOGIN);
        if (user != null) {
            return true;
        } else {
            String reqRootUrl = request.getRequestURL().toString();
//            String proName = request.getContextPath();
//            String methodType = request.getMethod();

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(JSON.toJSON(ResponseFactory.notLogin("用户未登录,不能访问!")));

            out.flush();
            out.close();
            return false;
        }
    }
}
