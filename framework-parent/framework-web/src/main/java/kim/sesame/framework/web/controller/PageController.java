package kim.sesame.framework.web.controller;

import kim.sesame.framework.entity.GMap;
import kim.sesame.framework.utils.DateUtils;
import kim.sesame.framework.utils.Ipconfig;
import kim.sesame.framework.web.annotation.IgnoreLoginCheck;
import kim.sesame.framework.web.config.WebProperties;
import kim.sesame.framework.web.response.Response;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * 所有的页面加载
 */
@CommonsLog
@Controller
@RequestMapping("/page")
public class PageController extends AbstractWebController {

    @Resource
    private WebProperties webProperties;

    /**
     * 加载界面
     */
    @IgnoreLoginCheck
    @RequestMapping("/{uriPath}")
    public String index(@PathVariable("uriPath") String uriPath, HttpServletRequest request, ModelMap modelMap) {
        Map<String, String[]> mm = request.getParameterMap();
        for (String s : mm.keySet()) {
            modelMap.put(s, request.getParameter(s));
        }
        uriPath = uriPath.replaceAll(webProperties.getPageReplace(), "/");
        return uriPath;
    }

    @IgnoreLoginCheck
    @RequestMapping("/error")
    public String page404(String e, ModelMap modelMap) {
        modelMap.put("e", e);
        return "error";
    }


    @IgnoreLoginCheck
    @ResponseBody
    @RequestMapping("/date")
    public Response date() {
        GMap map = GMap.newMap();
        String date = DateUtils.convert(new Date());
        map.put("date", date);
        map.put("net-info", Ipconfig.getInfo());

        return returnSuccess(map);
    }

}