package kim.sesame.framework.util.upload.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;

/**
 * 文件上传业务接口
 */
public interface UploadService {

    /**
     * 文件上传
     *
     * @param sysCode    系统编号
     * @param moduleName 模块名称,或者编号
     * @param file       文件流
     * @return 返回文件地址
     */
    String uploadFileMethod(String sysCode, String moduleName, MultipartFile file);

    default String uploadFileMethod(String sysCode, String moduleName, InputStream in,String type) {
        return "";
    }

    /**
     * 获取文件全路径
     *
     * @param module
     * @param fileType
     * @return
     */
    default String getFullFile(String module, String fileType) {
        String datefile = DateUtil.format(new Date(), "yyyy/MM/dd");
        return module + "/" + datefile + "/" + getFileName(fileType);
    }

    /**
     * 生成文件名
     *
     * @param fileType
     * @return
     */
    default String getFileName(String fileType) {
        return IdUtil.simpleUUID() + fileType;
    }
}
