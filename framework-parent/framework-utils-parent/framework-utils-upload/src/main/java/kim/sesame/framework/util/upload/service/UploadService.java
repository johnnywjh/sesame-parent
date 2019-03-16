package kim.sesame.framework.util.upload.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传业务接口
 */
public interface UploadService {

    /**
     * 文件上传
     * @param sysCode 系统编号
     * @param moduleName 模块名称,或者编号
     * @param file 文件流
     * @return 返回文件地址
     */
    default String uploadFileMethod(String sysCode, String moduleName, MultipartFile file) {

        return null;
    }
}
