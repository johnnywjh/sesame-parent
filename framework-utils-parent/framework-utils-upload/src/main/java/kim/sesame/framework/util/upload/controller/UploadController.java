package kim.sesame.framework.util.upload.controller;

import kim.sesame.common.result.ApiResult;
import kim.sesame.common.web.annotation.IgnoreLoginCheck;
import kim.sesame.common.web.config.ProjectConfig;
import kim.sesame.common.web.controller.AbstractWebController;
import kim.sesame.framework.util.upload.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * 文件上传控制层
 */
@Slf4j
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
    @IgnoreLoginCheck//(isLoadUser = true)
    @PostMapping("/file/{moduleName}")
    public ApiResult uploadFile(@PathVariable("moduleName") String moduleName, MultipartFile file) {
        String src = uploadService.uploadFileMethod(ProjectConfig.getSysCode(), moduleName, file);
        log.info("文件上传结果 src : {}", src);
        return ok(src);
    }


}
