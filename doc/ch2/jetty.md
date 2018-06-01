##### 修改pom.xml
```
        <dependency>
            <groupId>${sesame.groupId}</groupId>
            <artifactId>framework-web</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-tomcat</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jetty</artifactId>
        </dependency>
```
##### 切换成别的服务添加对应的依赖即可