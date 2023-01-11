- [运行方式](https://gitee.com/sesamekim/fms)

- 代码 UploadDefaultService.java
```java
import com.alibaba.fastjson.JSON;
import com.github.kevinsawicki.http.HttpRequest;
import com.sesame.web.upload.config.UploadProperties;
import kim.sesame.common.exception.BizException;
import kim.sesame.framework.util.upload.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * 文件上传的默认实现
 */
@Slf4j
public class UploadDefaultService implements UploadService {

    @Autowired
    private UploadProperties uploadProperties;

    @Override
    public String uploadFileMethod(String sysCode, String moduleName, MultipartFile file) {
        if (StringUtils.isEmpty(sysCode)) {
            sysCode = uploadProperties.getSysCode();
        }
        String url = uploadProperties.getFms().getUploadUrl();
        HttpRequest request = HttpRequest.post(url);
        try {
            request.part("file", file.getOriginalFilename(), "multipart/form-data;", file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        request.part("module", sysCode + "/" + moduleName);
        String result = request.body();
        log.info("返回信息为：" + result);
        
        try {
            JSONObject json = JSONObject.parseObject(result);
            return json.getString("data");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException("上传服务暂不可使用,请稍后重试！");
        }
    }
}

```