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

- 代码 [MinioUtils.java](https://www.cnblogs.com/ludangxin/p/15178147.html)
