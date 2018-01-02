package com.sesame.framework.utils;

import com.github.kevinsawicki.http.HttpRequest;
import com.sesame.framework.entity.GMap;
import net.sf.json.JSONObject;

/**
 * 图片上传
 */
public class FileUploadUitls {
    final static String upload_url = "http://www.sesame.kim/fss/index/upload";

    /**
     * 图片上传
     *
     * @param str         文件base64 字符串
     * @param projectName 项目名
     * @param moduleName  模块名
     * @param type        文件类型(文件后缀)
     * @return 返回json
     */
    public static JSONObject upload(String str, String projectName, String moduleName, String type) {
        GMap params = GMap.newMap();
        params.putAction("file", str);
        params.putAction("projectName", projectName);
        params.putAction("moduleName", moduleName);
        params.putAction("type", type);
        String body = HttpRequest.post(upload_url).form(params).body();

        return JSONObject.fromObject(body);
    }

    /**
     * 图片上传
     *
     * @param str         文件base64 字符串
     * @param projectName 项目名
     * @param moduleName  模块名
     * @param type        文件类型(文件后缀)
     * @return 返回 src 地址
     */
    public static String uploadResSrc(String str, String projectName, String moduleName, String type) {

        JSONObject json = upload(str, projectName, moduleName, type);
        json = json.getJSONObject("result");
        String src = json.get("service").toString() + json.get("path").toString();

        return src;
    }
}
