### 1 在pom中添加
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <optional>true</optional>
</dependency>
```

### 2 如果使用的是 thymeleaf 模板,那么在 application.properties 里添加,建议开发环境
```
spring.thymeleaf.cache=false
```

### 3 idea 配置
![输入图片说明](https://gitee.com/uploads/images/2017/1224/120237_ad3599de_1599674.png "屏幕截图.png")


### 然后 Shift+Ctrl+Alt+/，选择Registry 
![输入图片说明](https://gitee.com/uploads/images/2017/1224/120343_cb4c196f_1599674.png "屏幕截图.png")

### 再然后 勾上这个选项 
![输入图片说明](https://gitee.com/uploads/images/2017/1224/120431_1c8f6d3c_1599674.png "屏幕截图.png")

#### ok了，重启一下项目，然后改一下类或是html里面的内容，IDEA就会自动去make了。