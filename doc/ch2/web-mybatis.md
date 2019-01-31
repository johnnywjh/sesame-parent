### spring boot 作为基础,然后整合mvc 和mybaits,新建项目就不说了, 从 web项目上直接改
- 新建项目 spring-boot-example-ssm

#### 1 pom.xml 中新增如下依赖
```
<dependency>
     <groupId>${sesame.groupId}</groupId>
     <artifactId>framework-mybatis</artifactId>
</dependency>
```

##### framework-mybatis中
- 所有的配置在 DruidProperties.java 里
- xml的默认扫描方式 classpath*:**/dao/*DaoMapping.xml , 可通过配置sesame.framework.db.mapper-path=   修改
- 分页插件采用 PageHelper [文档地址](https://gitee.com/free/Mybatis_PageHelper)

#### 2 application.properties 添加数据库配置
```
spring.datasource.url=jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8
spring.datasource.username=testacc
spring.datasource.password=123456
```

#### 代码地址 [spring-boot-example-ssm](https://gitee.com/sesamekim/demo/tree/master/spring-boot-example/spring-boot-example-ssm)