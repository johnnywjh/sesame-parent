### 文件上传的使用方式

- 配置
```yaml
ex:
  upload:
    minio:
      enable: true
      endpoint: node1.cn
      port: 9000
      default-bucket-name: public
      access-key: miniouser_access_Key
      secret-key: miniouser_secret_Key
    fms:
      enable: false
      upload-url: http://node1.cn:8072/upload
```
- 代码
```java
import com.sesame.web.upload.service.UploadDefaultService;
import com.sesame.web.upload.service.UploadTenxunService;
import com.sesame.web.upload.service.minio.UploadMinioService;
import kim.sesame.framework.util.upload.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文件上传模块启动配置
 */
@Configuration
@Slf4j
public class UploadConfig {

    @Autowired
    UploadProperties uploadProperties;

    @Bean
    public UploadService fileUploadBeanLoad() {

        UploadService bean = null;

        UploadDefaultService uploadDefaultService = new UploadDefaultService();

        boolean flgFms = uploadProperties.getFms().isEnable();
        boolean flgMinio = uploadProperties.getMinio().isEnable();

        if (flgFms == flgTenXun) {
            bean = uploadDefaultService;
        }
        if (flgFms) {
            bean = uploadDefaultService;
        }
        if (flgMinio) {
            bean = new UploadMinioService();
            UploadMinioService.init(uploadProperties);
        }
        log.info("---------------------------------");
        log.info("文件上传实例:" + bean.getClass());
        log.info("---------------------------------");
        return bean;
    }


}
```

```java
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件地址上传配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "ex.upload")
public class UploadProperties {

    /**
     * 系统编号,地址的第一个目录
     */
    private String sysCode = "sysCode";

    private Fms fms = new Fms();
    private Minio minio = new Minio();
    private Tenxun tenxun = new Tenxun();

    /**
     * 默认上传
     */
    @Data
    public static class Fms {
        /**
         * 是否开启
         */
        private boolean enable = false;
        /**
         * 文件上传默认地址
         */
        private String uploadUrl = "http://localhost:8072/upload";
    }

    /**
     * Minio
     * https://www.cnblogs.com/ludangxin/p/15178147.html
     */
    @Data
    public static class Minio {
        /**
         * 是否开启
         */
        private boolean enable = false;

        /**
         * 端点
         */
        private String endpoint;

        /**
         * 端口
         */
        private Integer port;

        /**
         * 默认的桶名称
         */
        private String defaultBucketName;

        /**
         * 访问key
         */
        private String accessKey;

        /**
         * 密钥
         */
        private String secretKey;
    }
}

```

- 1. [fms.md](README-fms.md)
- 1. [Minio](README-Minio.md)
