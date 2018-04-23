### framework-boot
- 写这个架构的初衷是: 易开发,易维护,易扩展,高可用
- 然后自己封装的一些spring-boot cloud 的东西,用起来方便,特此记录
- 文档地址 **[Wiki](https://gitee.com/sesamekim/framework-boot/wikis/pages)**
- 使用前需要看看的文档 : **[帮助文档](https://gitee.com/sesamekim/framework-boot/wikis/pages?title=%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E&parent=)**
- 查看maven中最新版本 : [kim.sesame](http://search.maven.org/#search%7Cga%7C1%7Ckim.sesame)
- QQ交流群 : 661013788   欢迎大家给我提建议

#### 其他项目地址
- demo 项目地址 : [https://gitee.com/sesamekim/demo](https://gitee.com/sesamekim/demo)
- 代码生成工具 : [https://gitee.com/sesamekim/codeGenerate](https://gitee.com/sesamekim/codeGenerate)
- 通用sso : [https://gitee.com/sesamekim/sso-web](https://gitee.com/sesamekim/sso-web) 

#### 更新日志

#### 1.3 `2018-4-23 `
1. 优化 SpringContextUtil 内上下文对象的注入方式,无需手动注入
2. 删除 framework-distributed-lock 模块
3. 整体重写 cache 和 cache-redis 模块,不兼容之前的版本 : [文档详情](https://gitee.com/sesamekim/framework-boot/wikis/pages?title=2.2%20%E7%BC%93%E5%AD%98-redis%20-%5B1.3%5D&parent=%E6%9E%B6%E6%9E%84%E7%BB%84%E4%BB%B6%E4%BD%BF%E7%94%A8)
4. IuserCache 添加注销方法
5. 重新定义 IUser,IRole 类,
6. 1.3版本升级spring-boot 为 1.5.12, 从1.4开始开始使用spring-boot 2.x 


#### 1.21 `2018-3-8`
1. 优化当前项目 groupId 不是 kim.sesame.framework 不一致导致的依赖问题

#### 1.2 `2018-3-7`
1. 插件 zip 支持多个路径的写法
2. 修改了整体结构,并更新为1.2

#### 1.0.0,1.0.1,1.1  `2018-3-1`
1. 这三个版本写写一起把,改动很多
2. 添加maven自定义插件模块 [文档地址](https://gitee.com/sesamekim/framework-boot/wikis/pages?title=1-%E9%87%8D%E6%96%B0%E5%8A%A0%E8%BD%BD%E9%9D%99%E6%80%81%E8%B5%84%E6%BA%90%E6%96%87%E4%BB%B6&parent=%E8%87%AA%E5%AE%9A%E4%B9%89maven%E6%8F%92%E4%BB%B6)

