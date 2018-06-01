### spring boot 作为基础,然后整合mvc 和mybaits,新建项目就不说了, 从(2.1) web项目上马直接改

#### 1 pom.xml 中新增如下依赖
```
<dependency>
     <groupId>${sesame.groupId}</groupId>
     <artifactId>framework-mybatis</artifactId>
</dependency>
```

##### framework-mybatis中
- 所有的配置在 DruidProperties.java 里
- xml的默认扫描方式 classpath*:com/sesame/**/dao/*.xml , 可通过配置sesame.framework.mybatis.mapper-path=   修改
- 分页插件采用 PageHelper [文档地址](https://gitee.com/free/Mybatis_PageHelper)

#### 2 application.properties 添加数据库配置
```
spring.datasource.url=jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8
spring.datasource.username=testacc
spring.datasource.password=123456
```

#### 好了, 到这里就结束了,下一章会以一张测试表来讲解详细的代码结构