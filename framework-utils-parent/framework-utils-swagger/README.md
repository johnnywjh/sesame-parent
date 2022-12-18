### swagger使用
> 参考文档
- https://blog.csdn.net/zhaotaotao8/article/details/122542753
- https://gitee.com/xiaoym/knife4j


#### 配置
```yaml
kim:
  swagger:
    enable: true
    basePackages:
      - com.xxxxx.controller
    title: 用户系统
    version: v1.0
#    description: descriptionXXX
#    termsOfServiceUrl: http://termsOfServiceUrlXXXX
#    contact:
#      name: 联系人
#      url: http://xxxxxxx.com
#      email: xxxxxxx@qq.com 
```
 
#### 说明
```text
<!--        <dependency>-->
<!--            <groupId>io.springfox</groupId>-->
<!--            <artifactId>springfox-swagger2</artifactId>-->
<!--            <version>3.0.0</version>-->
<!--        </dependency>-->
<!--        <dependency>-->
<!--            <groupId>io.springfox</groupId>-->
<!--            <artifactId>springfox-swagger-ui</artifactId>-->
<!--            <version>3.0.0</version>-->
<!--        </dependency>-->

<!--        <dependency>-->
<!--            <groupId>io.springfox</groupId>-->
<!--            <artifactId>springfox-boot-starter</artifactId>-->
<!--            <version>${swagger.version}</version>-->
<!--        </dependency>-->

        <dependency>
            <groupId>com.github.xiaoymin</groupId>
            <artifactId>knife4j-spring-boot-starter</artifactId>
            <version>${knife4j.version}</version>
        </dependency>
```
