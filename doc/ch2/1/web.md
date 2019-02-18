> 一个简单的web项目

### 1 项目结构说明,springboot的默认配置
```
src     
| |-main
| | |-java  // 源码目录
| | |-resources  // 资源,配置文件目录
| | |   |-config  // 配置文件目录
| | |   |-public  // 一般存放项目首页
| | |   |-static  // 一般存放项目静态资源
| | |   |-templates  // 模板 html 页面存放处
_pom.xml 
```
### 2 pom.xml 这里只介绍关键配置,其余的略过
- 创建一个maven项目 spring-boot-example-web

#### 2.1 父模块坐标(后面所有的项目都有这个) 
```
    <parent>
        <groupId>kim.sesame.framework</groupId>
        <artifactId>sesame-parent</artifactId>
        <version>${kim.last.version}</version>
    </parent>
```

#### 2.2 web项目依赖
```
        <dependency>
            <groupId>${sesame.groupId}</groupId>
            <artifactId>framework-web</artifactId>
        </dependency>
```

### 3 项目配置
#### 3.1 在config目录中新建application.properties文件,关键配置如下
```
server.port=8080
```

#### 3.2 启动类, 在src/main/java 目录下新建 App.java
文件
```
package com.sesame;

import kim.sesame.framework.web.context.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目启动时, 运行这个main方法就好
 */
@SpringBootApplication
public class WebApp {

    public static void main(String[] args) {
        SpringApplication.run(WebApp.class, args);
        SpringContextUtil.printInfo();
    }

}

```
#### 3.3 页面编写, 在 public 目录中新建一个 index.html 文件,内容如下
```
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>index</title>
</head>
<body>
<h1>A simple web project</h1>
</body>
</html>
```

##### ok 一个简单的web项目就完成了,现在我们来启动App类,然后就可以看到效果

![输入图片说明](https://gitee.com/uploads/images/2017/1222/213611_00e9c5aa_1599674.png "屏幕截图.png")

#### 代码地址 [spring-boot-example/spring-boot-example-web](https://gitee.com/sesamekim/demo/tree/master/spring-boot-example/spring-boot-example-web)