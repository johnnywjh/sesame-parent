package kim.sesame.common.web.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.kevinsawicki.http.HttpRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * ip 信息
 * Description:
 */
public class IPUitl {

    /**
     * 获取客户端的ip
     *
     * @param request request
     */
    public static String getIp(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }
        return request.getHeader("x-forwarded-for");
    }
    /***
     * 获取IP
     * @param request
     * @return
     */
    public static String getIp(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getAddress().getHostAddress();
        }
        return ip;
    }

    /**
     * 获取地址
     *
     * @param ip ip
     * @return map
     * @throws Exception Exception
     */
    public static Map<String, String> getAddress(String ip) throws Exception {
        Map<String, String> mm = new HashMap<String, String>();
        String path = "http://ip.taobao.com/service/getIpInfo.php?ip=" + ip;

        String res = HttpRequest.get(path).body();

        JSONObject jb = JSON.parseObject(res);
        //{"code":0,"data":{"country":"中国","country_id":"CN","area":"华南","area_id":"800000","region":"广东省","region_id":"440000","city":"深圳市","city_id":"440300","county":"","county_id":"-1","isp":"联通","isp_id":"100026","ip":"112.90.78.25"}}
        //{"code":0,"data":{"country":"中国","country_id":"CN","area":"华东","area_id":"300000","region":"浙江省","region_id":"330000","city":"杭州市","city_id":"330100","county":"","county_id":"-1","isp":"阿里云","isp_id":"1000323","ip":"121.40.219.5"}}
        System.out.println(jb.toString());
        String code = jb.getString("code");
        if (code.equals("0")) {
            jb = jb.getJSONObject("data");
            mm.put("country", jb.getString("country"));
            mm.put("region", jb.getString("region"));
            mm.put("city", jb.getString("city"));
            mm.put("country", jb.getString("country"));
            mm.put("country", jb.getString("country"));
        } else {
            System.out.println("无效的ip");
        }
        return mm;
    }


}
