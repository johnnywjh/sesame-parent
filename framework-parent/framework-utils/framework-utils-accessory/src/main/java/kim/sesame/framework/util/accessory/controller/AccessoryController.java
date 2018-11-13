package kim.sesame.framework.util.accessory.controller;

import com.alibaba.fastjson.JSON;
import kim.sesame.framework.util.accessory.bean.Accessory;
import kim.sesame.framework.util.accessory.service.AccessoryService;
import kim.sesame.framework.utils.StringUtil;
import kim.sesame.framework.web.config.ProjectConfig;
import kim.sesame.framework.web.controller.AbstractWebController;
import kim.sesame.framework.web.response.Response;
import kim.sesame.framework.web.upload.service.UploadFileService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * AccessoryController
 *
 * @author wangjianghai
 * @date 2018-07-23 16:54:27
 * @Description: 附件表
 */
@CommonsLog
@RestController
@RequestMapping("/accessory")
public class AccessoryController extends AbstractWebController {
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private AccessoryService accessoryService;// 附件

    /**
     * 上传附件
     *
     * @param file
     * @return
     */
    @RequestMapping("/upload")
    public Response upload(MultipartFile file, HttpServletRequest request) {
        String fileName = null;
        if (request.getCharacterEncoding() == null) {
            try {
                fileName = new String(file.getOriginalFilename().getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            log.debug("1 获取到上传文件的文件名称"+fileName);
        }else{
            fileName = file.getOriginalFilename();
            log.debug("2 获取到上传文件的文件名称"+fileName);
        }

        String s = uploadFileService.uploadFileMethod(ProjectConfig.getSysCode(), "f", file);

        Map map = JSON.parseObject(s, Map.class);
        map = JSON.parseObject(map.get("result").toString(), Map.class);
        String src = map.get("src").toString();

        Accessory bean = accessoryService.add(fileName, src);
        return returnSuccess(bean);
    }

}
