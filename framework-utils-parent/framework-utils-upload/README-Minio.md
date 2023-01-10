> 部署
```shell
docker run -d --restart=always \
  -p 9000:9000 -p 9090:9090 \
  --name minio \
  -e "MINIO_ACCESS_KEY=minioadmin" \
  -e "MINIO_SECRET_KEY=minioadmin" \
  -v /opt/minio/data:/data \
  -v /opt/minio/config:/root/.minio \
  minio/minio server /data --console-address ":9090" -address ":9000"
  
## 访问
http://node1.cn:9090/login
```

- docker-compose
```yaml
version: "3"

services:

  minio:
    container_name: minio
    command: server /data --console-address ":9090" -address ":9000"
    image: minio/minio
    restart: always
    ports:
      - 9000:9000
      - 9090:9090
    volumes:
      - /opt/minio/data:/data
      - /opt/minio/config:/root/.minio
    environment:
      MINIO_ACCESS_KEY: minioadmin
      MINIO_SECRET_KEY: minioadmin

```

- 代码
```xml
<okhttp3.version>4.10.0</okhttp3.version>
       <dependency>
            <groupId>com.squareup.okhttp3</groupId>
            <artifactId>okhttp</artifactId>
            <version>${okhttp3.version}</version>
        </dependency>
        <dependency>
            <groupId>io.minio</groupId>
            <artifactId>minio</artifactId>
            <version>8.5.0</version>
        </dependency>
```

- 代码 MinioUtils.java
```java
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * minio 工具类
 */
@Slf4j
@Component
public class MinioUtils {

    private static String defaultBucketName;
    private static MinioClient minioClient;

    public static void setDefaultBucketName(String defaultBucketName) {
        MinioUtils.defaultBucketName = defaultBucketName;
    }

    public static void setMinioClient(MinioClient minioClient) {
        MinioUtils.minioClient = minioClient;
    }

    /**
     * 获取全部bucket
     *
     * @return all bucket
     */
    @SneakyThrows
    public List<Bucket> getAllBuckets() {
        return minioClient.listBuckets();
    }

    /**
     * 判断 bucket是否存在
     *
     * @param bucketName 桶名称
     * @return true 存在
     */
    @SneakyThrows
    public boolean bucketExists(String bucketName){
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder()
                .bucket(bucketName)
                .build();
        return minioClient.bucketExists(bucketExistsArgs);
    }

    /**
     * 创建 bucket
     *
     * @param bucketName 桶名称
     */
    @SneakyThrows
    public void createBucket(String bucketName){
        boolean isExist = this.bucketExists(bucketName);
        if(!isExist) {
            MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build();
            minioClient.makeBucket(makeBucketArgs);
        }
    }

    /**
     * 文件上传
     *
     * @param bucketName 桶名称
     * @param fileName 上传后的文件名称
     * @param fileAbsolutePath 文件的绝对路径
     * @return 文件url
     */
    @SneakyThrows
    public ObjectWriteResponse upload(String bucketName, String fileName, String fileAbsolutePath){
        UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                .bucket(bucketName)
                .filename(fileAbsolutePath)
                .object(fileName)
                .build();
        return minioClient.uploadObject(uploadObjectArgs);
    }

    /**
     * 文件上传
     *
     * @param fileName 上传后的文件名称
     * @param stream 文件输入流
     * @return 文件url
     */
    @SneakyThrows
    public String upload(String fileName, InputStream stream){
        this.upload(defaultBucketName, fileName, stream);
        return getFileUrl(defaultBucketName, fileName);
    }

    /**
     * 文件上传
     *
     * @param bucketName 桶名称
     * @param fileName 上传后的文件名称
     * @param stream 文件输入流
     * @return 文件url
     */
    @SneakyThrows
    public ObjectWriteResponse upload(String bucketName, String fileName, InputStream stream){
        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(stream, stream.available(), -1)
                    .build();
            return minioClient.putObject(objectArgs);
        }
        catch(Exception e) {
            log.error(e.getMessage());
        }
        finally {
            try {
                if(Objects.nonNull(stream)) {
                    stream.close();
                }
            } catch(IOException e) {
                log.error(e.getMessage());
            }
        }
        return null;
    }

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 文件url
     */
    public ObjectWriteResponse upload(MultipartFile file) {
        return this.upload(defaultBucketName, file);
    }

    /**
     * 文件上传
     *
     * @param bucketName 桶名称
     * @param file 文件
     * @return 文件url
     */
    public ObjectWriteResponse upload(String bucketName, MultipartFile file) {
        InputStream is = null;
        try {
            is = file.getInputStream();
            final String fileName = file.getOriginalFilename();
            String contentType = file.getContentType();
            PutObjectArgs objectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(is, is.available(), -1)
                    .contentType(contentType)
                    .build();
            return minioClient.putObject(objectArgs);
        }
        catch(Exception e) {
            log.error(e.getMessage());
        }
        finally {
            try {
                if(Objects.nonNull(is)) {
                    is.close();
                }
            } catch(IOException e) {
                log.error(e.getMessage());
            }
        }
        return null;
    }

    /**
     * 附件下载
     *
     * @param fileName 附件名称
     */
    @SneakyThrows
    public void download(String fileName, HttpServletResponse response) {
        this.download(defaultBucketName, fileName, response);
    }

    /**
     * 附件下载
     *
     * @param bucketName 桶名称
     * @param fileName 附件名称
     */
    @SneakyThrows
    public void download(String bucketName, String fileName, HttpServletResponse response) {
        GetObjectArgs build = GetObjectArgs.builder().bucket(bucketName).object(fileName).build();
        OutputStream out = null;
        try(GetObjectResponse object = minioClient.getObject(build)) {
            int len = 0;
            byte[] buffer = new byte[1024];
            out = response.getOutputStream();
            response.reset();
            String fileName1 = new String(fileName.getBytes(), StandardCharsets.ISO_8859_1);
            response.addHeader("Content-Disposition", " attachment;filename=" + fileName1);
            response.setContentType("application/octet-stream");

            while((len = object.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch(Exception e) {
            log.error(e.getMessage());
        } finally {
            if(out != null) {
                try {
                    out.close();
                } catch(IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    /**
     * 附件下载
     *
     * @param fileName 附件名称
     */
    @SneakyThrows
    public void download(String bucketName, String fileName, String fileAbsolutePath) {
        DownloadObjectArgs downloadObjectArgs = DownloadObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .filename(fileAbsolutePath)
                .build();
        minioClient.downloadObject(downloadObjectArgs);
    }

    /**
     * 删除文件
     *
     * @param fileName 文件名称
     */
    @SneakyThrows
    public void delFile(String fileName){
        this.delFile(defaultBucketName, fileName);
    }

    /**
     * 删除文件
     *
     * @param bucketName 桶名称
     * @param fileName 文件名称
     */
    @SneakyThrows
    public void delFile(String bucketName, String fileName){
        RemoveObjectArgs removeObjectsArgs = RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build();
        minioClient.removeObject(removeObjectsArgs);
    }

    /**
     * 获取minio文件的下载地址
     *
     * @param fileName 文件名
     */
    @SneakyThrows
    public String getFileUrl(String fileName) {
        return this.getFileUrl(defaultBucketName, fileName);
    }

    /**
     * 获取minio文件的下载地址
     *
     * @param bucketName 桶名称
     * @param fileName 文件名
     */
    @SneakyThrows
    public String getFileUrl(String bucketName, String fileName) {
        GetPresignedObjectUrlArgs objectUrlArgs = GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(fileName)
                .build();
        return minioClient.getPresignedObjectUrl(objectUrlArgs);
    }

    /**
     * 获取minio文件的下载地址
     *
     * @param fileName 文件名
     */
    @SneakyThrows
    public String getFileUrl(String fileName, Integer duration, TimeUnit unit) {
        return this.getFileUrl(defaultBucketName, fileName, duration, unit);
    }

    /**
     * 获取minio文件的下载地址
     *
     * @param bucketName 桶名称
     * @param fileName 文件名
     */
    @SneakyThrows
    public String getFileUrl(String bucketName, String fileName, Integer duration, TimeUnit unit) {
        GetPresignedObjectUrlArgs objectUrlArgs = GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(fileName)
                .expiry(duration, unit)
                .build();
        return minioClient.getPresignedObjectUrl(objectUrlArgs);
    }

    /**
     * 设置桶策略
     *
     * @param bucketName 桶名称
     * @param policy 策略
     */
    @SneakyThrows
    public void setBucketPolicy(String bucketName, String policy) {
        SetBucketPolicyArgs bucketPolicyArgs = SetBucketPolicyArgs.builder()
                .bucket(bucketName)
                .config(policy)
                .build();
        minioClient.setBucketPolicy(bucketPolicyArgs);
    }

}
```

- 代码 UploadMinioService.java
```java
import com.sesame.web.upload.config.UploadProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import kim.sesame.common.web.context.SpringContextUtil;
import kim.sesame.framework.util.upload.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
 * Minio
 */
@Slf4j
public class UploadMinioService implements UploadService {

    @Autowired
    private UploadProperties uploadProperties;
    @Autowired
    private MinioUtils minioUtils;

    public static void init(UploadProperties upload) {
        if (upload == null) {
            upload = SpringContextUtil.getBean(UploadProperties.class);
        }
        if (upload == null) {
            return;
        }
        UploadProperties.Minio minioProperties = upload.getMinio();
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minioProperties.getEndpoint(), minioProperties.getPort(), false)
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
        String defaultBucketName = minioProperties.getDefaultBucketName();

        if (Objects.nonNull(defaultBucketName)) {
            BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder()
                    .bucket(defaultBucketName)
                    .build();
            try {
                // 创建默认的bucket
                if (!minioClient.bucketExists(bucketExistsArgs)) {
                    MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder()
                            .bucket(defaultBucketName)
                            .build();
                    minioClient.makeBucket(makeBucketArgs);
                    log.info("create default bucket \"{}\" success", defaultBucketName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        MinioUtils.setDefaultBucketName(defaultBucketName);
        MinioUtils.setMinioClient(minioClient);
    }

    @Override
    public String uploadFileMethod(String sysCode, String moduleName, MultipartFile file) {
        if (StringUtils.isEmpty(sysCode)) {
            sysCode = uploadProperties.getSysCode();
        }
        // 指定要上传到 COS 上对象键
        String key = sysCode + "_" + moduleName;

        String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = getFullFile(key, fileType);
        try {
            log.info("fileName : {}", fileName);
            String url = minioUtils.upload(fileName, file.getInputStream());
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

```