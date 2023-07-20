集成步骤
1. 项目引入 druid-spring-boot-starter 依赖。
- https://mvnrepository.com/artifact/com.alibaba/druid
```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>${version}</version>
</dependency>
```
2. 排除原生Druid的快速配置类。
注意：v3.3.3及以上版本不用排除了。
```java
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
public class Application {

    public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
    }
}
```

某些springBoot的版本上面可能无法排除可用以下方式排除。

```
spring:
autoconfigure:
exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure
```