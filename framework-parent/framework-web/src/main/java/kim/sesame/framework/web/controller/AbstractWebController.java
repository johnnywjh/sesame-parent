package kim.sesame.framework.web.controller;


import kim.sesame.framework.utils.StringUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
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

    public void download(String fileName, String path, HttpServletResponse response) {
        try {
            // 实现文件下载
            response.setContentType("text/plain");
            response.setHeader("Location", fileName);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
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
}
