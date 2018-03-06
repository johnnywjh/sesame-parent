package kim.sesame.framework.web.interceptor.auth;

import kim.sesame.framework.utils.StringUtil;
import kim.sesame.framework.web.controller.AbstractController;
import kim.sesame.framework.web.interceptor.annotation.AuthNonCheckRequired;
import kim.sesame.framework.web.response.Response;
import kim.sesame.framework.web.util.IPUitl;
import net.sf.json.JSONObject;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author johnny
 * @ClassName: AuthTokenInterceptor 
 * @Description: 使用拦截器验证请求终端的合法性，时效性等内容
 * @date 2016年9月27日 上午10:36:35  
 */
public class AuthTokenInterceptor extends HandlerInterceptorAdapter {

    /**
     * 验证用户的动态口令
     */
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        String reqRootUrl = req.getRequestURL().toString();
        String requestIp = "";
        try {
            HandlerMethod method = (HandlerMethod) handler;
            // 1.方法名称上有忽略注解的==>直接放行
            if (method.getMethod().isAnnotationPresent(AuthNonCheckRequired.class)) {
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
                    String public_token = req.getHeader("PUBLIC_TOKEN");
                    String auth_token = req.getHeader("AUTH_TOKEN");
                    if (StringUtil.isNotEmpty(public_token) && StringUtil.isNotEmpty(auth_token)) {
                        if (AuthTokenUtils.isValid(public_token, auth_token)) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Response response = Response.create().
                setExceptionType(AbstractController.ExceptionType.VALIDATOR)
                .setErrorCode(AbstractController.ErrorCode.VALIDATOR).setMessage("权限不足!").setResult(requestIp);

        res.setCharacterEncoding("UTF-8");
        res.setContentType("application/json; charset=utf-8");
        PrintWriter out = res.getWriter();
        out.print(response);
        out.flush();
        out.close();
        System.out.println(JSONObject.fromObject(response));
        System.err.println("验证失败，非法请求..........>>" + reqRootUrl);
        return false;
    }
}
