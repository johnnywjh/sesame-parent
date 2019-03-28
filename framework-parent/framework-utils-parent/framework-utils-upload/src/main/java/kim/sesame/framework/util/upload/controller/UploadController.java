package kim.sesame.framework.util.upload.controller;

import kim.sesame.framework.util.upload.service.UploadService;
import kim.sesame.framework.web.config.ProjectConfig;
import kim.sesame.framework.web.controller.AbstractWebController;
import kim.sesame.framework.web.response.Response;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


/**
 * 文件上传控制层
 */
@CommonsLog
@RestController
@RequestMapping("/upload")
public class UploadController extends AbstractWebController {

    @Autowired(required = false)
    private UploadService uploadService;

    /**
     * 普通文件上传
     *
     * @param moduleName 模块名称
     * @param file       文件流
     * @return
     */
    @RequestMapping("/file")
    public Response uploadFile(String moduleName, MultipartFile file) {
        String src = uploadService.uploadFileMethod(ProjectConfig.getSysCode(), moduleName, file);
        log.info("文件上传结果 src:"+src);
        return returnSuccess(src);
    }

    /**
     * layui格式文件上传
     *
     * @param file 文件流
     * @return
     */
    @RequestMapping("/layfile")
    public Map layfile(MultipartFile file) {
        String src = uploadService.uploadFileMethod(ProjectConfig.getSysCode(), "e", file);
        log.info("文件上传结果 src:"+src);
        return layuiUpload(src);
    }


}
