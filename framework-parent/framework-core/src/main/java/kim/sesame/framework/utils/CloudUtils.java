package kim.sesame.framework.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.kevinsawicki.http.HttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 注册中心中心操作
 */
public class CloudUtils {
    /**
     * 获取注册中心中状态时 UP 的服务列表
     *
     * @param registryUrl 注册中心地址 http://eureka:8090
     * @param appName     服务名称
     * @return
     */
    public static List<String> getServiceList(String registryUrl, String appName) {
        List<String> urls = new ArrayList<>();

        try {
            String url = registryUrl + "/eureka/apps/" + appName;

            String res = HttpRequest.get(url)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .body();
            JSONObject json = JSON.parseObject(res);
            JSONArray instances = json.getJSONObject("application").getJSONArray("instance");

            String serviceUrl = null;
            for (int i = 0; i < instances.size(); i++) {
                JSONObject instance = instances.getJSONObject(i);
                if (instance.getString("status").equals("UP")) {
                    serviceUrl = instance.getString("homePageUrl");

                    try {
                        int urlLength = serviceUrl.length();
                        if (serviceUrl.substring(urlLength - 1, urlLength).equals("/")) {
                            serviceUrl = serviceUrl.substring(0, urlLength - 1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    urls.add(serviceUrl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urls;
    }

    /**
     * 负载的算法,随机获取一个url 连接
     *
     * @param list
     * @return
     */
    public static String loadBalance(List<String> list) {
        if (list.size() == 0) {
            return "";
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        Random rand = new Random();
        int index = rand.nextInt(list.size());
        return list.get(index);
    }

}
