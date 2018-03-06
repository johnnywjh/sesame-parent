package kim.sesame.framework.web.controller;


import kim.sesame.framework.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Web controller基类
 *
 * @author johnny
 * @date 2017/10/23 20:13
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

}
