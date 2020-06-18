package kim.sesame.framework.web.interceptor.reqlog;

import com.alibaba.fastjson.JSONObject;
import kim.sesame.framework.utils.DateUtil;
import kim.sesame.framework.web.annotation.IgnoreReqLogPrint;
import kim.sesame.framework.web.context.LogProintContext;
import kim.sesame.framework.web.response.Response;
import kim.sesame.framework.web.util.IPUitl;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求日志打印拦截器
 */
@CommonsLog
public class PrintReqLogInterceptor extends HandlerInterceptorAdapter {

    /**
     * 请求之前
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

        log.info(getReqData(request));
        LogProintContext.getLogProintContext().setStartTime(new Date());
        LogProintContext.getLogProintContext().setIsIgnore(true);

        return true;
    }

    /**
     * controller 里的方法处理完之后
     *
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LogProintContext.getLogProintContext().setEndTime(new Date());

        if (LogProintContext.getLogProintContext().getIsIgnore()) {
            log.info(getResData(response));
        }
        // 清理线程数据
        LogProintContext.getLogProintContext().clean();
    }

    private String getReqData(HttpServletRequest request) {
        StringBuilder msg = new StringBuilder();
        try {

            msg.append("Inbound Message\n\n");
            msg.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
            msg.append("Address: ").append(request.getMethod()).append("\t").append(request.getRequestURL()).append("\n");
            msg.append("ClientIp: ").append(IPUitl.getRemortIP(request)).append("\n");
//            msg.append("QueryString: ").append(request.getQueryString()).append("\n");
            msg.append("Encoding: ").append(request.getCharacterEncoding()).append("\n");
            msg.append("Content-Type: ").append(request.getContentType()).append("\n");
            msg.append("Headers: ").append(new ServletServerHttpRequest(request).getHeaders()).append("\n");

            Map<String, String[]> mm = request.getParameterMap();
            Map<String, Object> params = new HashMap<>();
            for (String s : mm.keySet()) {
                params.put(s, request.getParameter(s));
            }
            msg.append("params: ").append(JSONObject.toJSONString(params)).append("\n");
            msg.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg.toString();
    }

    private String getResData(HttpServletResponse response) {
        StringBuilder msg = new StringBuilder();
        try {
            msg.append("Outbound Message\n\n");
            msg.append("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
//            msg.append(response.toString().replace("\r\n", "\t")).append("\n");
            double t = LogProintContext.getLogProintContext().getTimeConsuming();
            msg.append("接口耗时:").append(t).append(" 秒,当前时间:").append(DateUtil.dateToString(new Date(), null)).append("\n");
            Response res = LogProintContext.getLogProintContext().getResponse();

            if (res != null) {
                msg.append(JSONObject.toJSONString(res)).append("\n");
            }
            msg.append("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg.toString();
    }


}
