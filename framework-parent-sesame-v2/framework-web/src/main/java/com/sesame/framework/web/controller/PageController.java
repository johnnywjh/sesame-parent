package com.sesame.framework.web.controller;

import com.sesame.framework.web.config.WebProperties;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 所有的页面加载
 */
@CommonsLog
@Controller
@RequestMapping("/page")
public class PageController {

    @Resource
    private WebProperties webProperties;

    /**
     * 加载页面
     */
    @RequestMapping("/{uriPath}")
    public String index(@PathVariable("uriPath") String uriPath, HttpServletRequest request, ModelMap modelMap) {
        Map<String, String[]> mm = request.getParameterMap();
        for (String s : mm.keySet()) {
            modelMap.put(s, request.getParameter(s));
        }
        uriPath = uriPath.replaceAll(webProperties.getPageReplace(), "/");
        return uriPath;
    }

}