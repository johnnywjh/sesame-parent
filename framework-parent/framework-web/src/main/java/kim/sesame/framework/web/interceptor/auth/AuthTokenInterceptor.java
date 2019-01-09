package kim.sesame.framework.web.interceptor.auth;

import com.alibaba.fastjson.JSON;
import kim.sesame.framework.utils.StringUtil;
import kim.sesame.framework.web.annotation.IgnoreAuthCheck;
import kim.sesame.framework.web.response.Response;
import kim.sesame.framework.web.response.ResponseFactory;
import kim.sesame.framework.web.util.IPUitl;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author johnny
 * Description: 使用拦截器验证请求终端的合法性，时效性等内容
 * date :  2016年9月27日 上午10:36:35  
 */
public class AuthTokenInterceptor extends HandlerInterceptorAdapter {

    /**
     * 验证用户的动态口令
     */
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        //过滤掉静态资源
        if (handler instanceof org.springframework.web.servlet.resource.ResourceHttpRequestHandler) {
            return true;
        }

        String reqRootUrl = req.getRequestURL().toString();
        String requestIp = "";
        try {
            HandlerMethod method = (HandlerMethod) handler;
            // 1.方法名称上有忽略注解的==>直接放行
            if (method.getMethod().isAnnotationPresent(IgnoreAuthCheck.class)) {
                return true;
            }

            // 2.验证白名单用户,如果是白名单用户则直接放行
            requestIp = IPUitl.getRemortIP(req);
            if (StringUtil.isNotEmpty(requestIp)) {
                if (AuthTokenWhiteListUtils.isValid(requestIp)) {
                    System.out.println(requestIp + ";白名单用户，执行放行。");
                    return true;
                } else {
                    // 验证动态口令
                    String publicToken = req.getHeader(AuthTokenUtils.PUBLIC_TOKEN_KEY);//公钥
                    String dynamicToken = req.getHeader(AuthTokenUtils.DYNAMIC_TOKEN_KEY);//动态tokean
                    if (StringUtil.isNotEmpty(publicToken) && StringUtil.isNotEmpty(dynamicToken)) {
                        if (AuthTokenUtils.isValid(publicToken, dynamicToken)) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Response response = ResponseFactory.illegalRequest("签名验证错误,非法请求", requestIp);
        res.setCharacterEncoding("UTF-8");
        res.setContentType("application/json; charset=utf-8");
        PrintWriter out = res.getWriter();
        out.print(response);
        out.flush();
        out.close();
        System.out.println(JSON.toJSON(response));
        System.err.println("验证失败，非法请求..........>>" + reqRootUrl);
        return false;

    }
}
