package kim.sesame.framework.web.controller;


import kim.sesame.framework.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Web controller基类
 *
 * @author johnny
 * date :  2017/10/23 20:13
 **/
public class AbstractWebController extends AbstractController {

    public Map layuiUpload(String src) {
        return layuiUpload(src, null);
    }

    public Map layuiUpload(String src, String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("src", src);
        if (StringUtil.isNotEmpty(name)) {
            map.put("name", name);
        }
        Map<String, Object> res = new HashMap<>();
        res.put("code", 0);
        res.put("msg", "");
        res.put("data", map);
        return res;
    }

    public Map layuiSuccuss(Object obj) {
        Map<String, Object> res = new HashMap<>();
        res.put("code", 0);
        res.put("msg", "");
        res.put("data", obj);
        return res;
    }

    public void download(String fileName, String path, HttpServletRequest request, HttpServletResponse response) {
        try {

            setDownloadResponse(fileName, request, response);

            OutputStream outputStream = response.getOutputStream();
            InputStream inputStream = new FileInputStream(path);

            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            outputStream.write(b);

            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void download(String fileName, String path, HttpServletResponse response) {
        try {

            setDownloadResponse(fileName, response);

            OutputStream outputStream = response.getOutputStream();
            InputStream inputStream = new FileInputStream(path);

            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            outputStream.write(b);

            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDownloadResponse(String fileName, HttpServletRequest request, HttpServletResponse response) {
        try {
            fileName = getFileName(request, fileName);
            // 实现文件下载
            response.setContentType("text/plain");
            response.setHeader("Location", fileName);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDownloadResponse(String fileName, HttpServletResponse response) {
        try {
            // 实现文件下载
            response.setContentType("text/plain");
            response.setHeader("Location", fileName);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFileName(HttpServletRequest request, String fileName) {
        try {
            if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }
}
