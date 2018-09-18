package kim.sesame.framework.web.upload.controller;

import com.alibaba.fastjson.JSON;
import kim.sesame.framework.web.config.ProjectConfig;
import kim.sesame.framework.web.controller.AbstractWebController;
import kim.sesame.framework.web.response.Response;
import kim.sesame.framework.web.upload.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


/**
 * 功能描述：上传文件 控制层
 *
 * @author 01122649-吴志飞
 * @create on 2018-07-13 9:19
 * @since V1.0.0
 */
@RestController
@RequestMapping("/upload")
public class UploadFileController extends AbstractWebController {
    @Autowired
    private UploadFileService uploadFileService;

    /**
     * 普通文件上传
     *
     * @param moduleName 模块名称
     * @param file       文件流
     * @return
     */
    @RequestMapping("/file")
    public Response uploadFile(String moduleName, MultipartFile file) {
        String s = uploadFileService.uploadFileMethod(ProjectConfig.getSysCode(), moduleName, file);
        return JSON.parseObject(s, Response.class);
    }

    /**
     * layui格式文件上传
     *
     * @param file 文件流
     * @return
     */
    @RequestMapping("/layfile")
    public Map layfile(MultipartFile file) {
        String s = uploadFileService.uploadFileMethod(ProjectConfig.getSysCode(), "e", file);

        Map map = JSON.parseObject(s, Map.class);
        map = JSON.parseObject(map.get("result").toString(), Map.class);
        return layuiUpload(map.get("src").toString());
    }


}
