package kim.sesame.framework.web.interceptor.reqlog;

import com.alibaba.fastjson.JSONObject;
import kim.sesame.framework.web.annotation.IgnoreReqLogPrint;
import kim.sesame.framework.web.util.IPUitl;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求日志打印拦截器
 */
@CommonsLog
public class PrintReqLogInterceptor extends HandlerInterceptorAdapter {

    /**
     * 开启线程打印
     */
    public static boolean enableThreadProint = false;

    /**
     * 打印请求里的日志
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
            if (method.getMethod().isAnnotationPresent(IgnoreReqLogPrint.class)) {
                return true;
            }
        }

        if (enableThreadProint) {
            new Thread(() -> {
                logPrintln(request, response);
            }).start();
        } else {
            logPrintln(request, response);
        }

        return true;
    }

    private void logPrintln(HttpServletRequest request, HttpServletResponse response) {
        try {
            StringBuilder msg = new StringBuilder();
            if(enableThreadProint){
                msg.append("Inbound Message \n--------- new thread print -------------------\n");
            }else{
                msg.append("Inbound Message\n----------------------------\n");
            }

            msg.append("Address: ").append(request.getRequestURL()).append("\n");
            msg.append("ClientIp: ").append(IPUitl.getRemortIP(request)).append("\n");
            msg.append("QueryString: ").append(request.getQueryString()).append("\n");
            msg.append("Encoding: ").append(request.getCharacterEncoding()).append("\n");
            msg.append("Content-Type: ").append(request.getContentType()).append("\n");
            msg.append("Headers: ").append(new ServletServerHttpRequest(request).getHeaders()).append("\n");

            Map<String, String[]> mm = request.getParameterMap();
            Map<String, Object> params = new HashMap<>();
            for (String s : mm.keySet()) {
                params.put(s, request.getParameter(s));
            }
            msg.append("params: ").append(JSONObject.toJSONString(params)).append("\n");
            msg.append("----------------------------------------------");

            log.info(msg.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
