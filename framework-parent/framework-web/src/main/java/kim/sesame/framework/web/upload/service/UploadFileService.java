package kim.sesame.framework.web.upload.service;

import com.github.kevinsawicki.http.HttpRequest;
import kim.sesame.framework.web.config.ProjectConfig;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件上传操作
 */
@Service
@CommonsLog
public class UploadFileService {
    /**
     * 上传文件到服务器
     *
     * @param projectName 项目名称
     * @param moduleName  模块名称
     * @param file        文件
     * @return
     */
    public String uploadFileMethod(String projectName, String moduleName, MultipartFile file) {
        String url = ProjectConfig.getUploadUrl();
        HttpRequest request = HttpRequest.post(url);
        try {
            request.part("file", file.getOriginalFilename(), "multipart/form-data;", file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        request.part("projectName", projectName);
        request.part("moduleName", moduleName);
        String result = request.body();
        log.info("返回信息为：" + result);
        return result;
    }
}
