> 如果大家用idea 的话,需要安装一下lombok 插件,它的作用可以让idea 支持一些注解
我一般用下面这两个
- @Data (import lombok.Data;) 实体类省略 get set 方法,生成 class 文件时,会自动生成
- @CommonsLog  给当前类注入 log 对象
- 帮助文档 [https://projectlombok.org/features/all](https://projectlombok.org/features/all)
- [http://himichaelchu.iteye.com/category/324280](http://himichaelchu.iteye.com/category/324280)

##### 安装方法
![输入图片说明](https://gitee.com/uploads/images/2017/1228/092209_c185d279_1599674.png "屏幕截图.png")

##### 另外我项目里有一个依赖
```
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<version>1.18.6</version>
		</dependency>
```