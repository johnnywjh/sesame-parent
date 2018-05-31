#### 其他项目地址
- demo 项目地址 : [https://gitee.com/sesamekim/demo](https://gitee.com/sesamekim/demo)
- 代码生成工具 : [https://gitee.com/sesamekim/codeGenerate](https://gitee.com/sesamekim/codeGenerate)
- 通用sso : [https://gitee.com/sesamekim/sso-web](https://gitee.com/sesamekim/sso-web) 

#### 更新日志

#### 1.4 `孵化中`
1. spring-boot 版本升级到 2.x,代码在分支1.4中
2. [升级修改事项](https://gitee.com/sesamekim/framework-boot/blob/master/doc/spring-boot-2.x.md)
3. 2.0尝试了一下,发现对springcloud不支持,所以现在静静的等待官网更新.[参考资料](https://blog.csdn.net/wd2014610/article/details/79632219)

#### 1.3.1 `2018-5-8 `
1. 废弃 AbstractUserCache 这个空实现类,采用java8 的接口 default方法
2. 新增 websocket 模块,暂时提供了单机支持,集群需要自己去实现
3. 修改对称加密,优化线程安全 EncryptionAndDecryption.java
4. 新增Excel解析类 XLSXCovertCSVReader.java(framework-core)
5. 更改时间类型参数传到后台如果值为空,返回 new Date()问题,现在返回 null
6. framework-core 增加运行环境判断工具类,VifRunEnv.isJar(Class clazz);
7. framework-web 增加签名校验,动态tokean校验,时间控制,使用文档还没有写.懒一会
8. framework-cache-redis 增加多redis实例支持, [#详见文档](https://gitee.com/sesamekim/framework-boot/wikis/pages?title=2.2-redis%E5%A4%9A%E5%AE%9E%E4%BE%8B%E9%85%8D%E7%BD%AE&parent=%E6%9E%B6%E6%9E%84%E7%BB%84%E4%BB%B6%E4%BD%BF%E7%94%A8)

#### 1.3 `2018-4-23 `
1. 优化 SpringContextUtil 内上下文对象的注入方式,无需手动注入
2. 删除 framework-distributed-lock 模块
3. 整体重写 cache 和 cache-redis 模块,不兼容之前的版本 : [#详见文档](https://gitee.com/sesamekim/framework-boot/wikis/pages?title=2.2%20%E7%BC%93%E5%AD%98-redis%20-%5B1.3%5D&parent=%E6%9E%B6%E6%9E%84%E7%BB%84%E4%BB%B6%E4%BD%BF%E7%94%A8)
4. IuserCache 添加注销方法
5. 重新定义 IUser,IRole 类,
6. 1.3版本升级spring-boot 为 1.5.12


#### 1.21 `2018-3-8`
1. 优化当前项目 groupId 不是 kim.sesame.framework 不一致导致的依赖问题

#### 1.2 `2018-3-7`
1. 插件 zip 支持多个路径的写法
2. 修改了整体结构,并更新为1.2

#### 1.0.0,1.0.1,1.1  `2018-3-1`
1. 这三个版本写写一起把,改动很多
2. 添加maven自定义插件模块 [文档地址](https://gitee.com/sesamekim/framework-boot/wikis/pages?title=1-%E9%87%8D%E6%96%B0%E5%8A%A0%E8%BD%BD%E9%9D%99%E6%80%81%E8%B5%84%E6%BA%90%E6%96%87%E4%BB%B6&parent=%E8%87%AA%E5%AE%9A%E4%B9%89maven%E6%8F%92%E4%BB%B6)
