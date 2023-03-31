
#### 更新日志

#### 2.6.11 `2022-12-18`
- 重写swagger公共代码
- 优化登录流程相关代码

#### 2.6.11 `2022-11-18`
- 版本和springboot看齐

#### 2.4.0 `2022-03-09`
- 修改项目结构,扁平化

#### 2.3.0 `2021-06-18`
- 该版本不兼容之前的版本,一次很大的修改
- springboot 版本升级到 2.4.x 
- 新增 framework-define 模块,用于定于各种实体类,底层有两个依赖
- 大部分模块更名,符合 spring-boot-starter 的规范
- 去掉了之前没有什么用的代码, 并优化了大部分代码,升级了所有工具依赖的版本
- 重写了加密算法模块
- 修改docker插件的配置,使得配置更自由
- 去掉PageHelp分页组件,因为兼容性问题.
- 整理mybatisplus的分页插件
- 分布式锁改为官网的 starter
- 缓存模块修改, 暂时的想法:去掉jedis,继续使用自带的
- 更新本地缓存组件的使用方式

> 预计待修改的内容 `一次很大的修改`

- 重写或者去掉 rocketmq模块, 要加上幂等的解决方案
- 拆分核心模块,添加自由组装pom, 方便引入. 兼容alibaba cloud
- 设计网关模块
- IEntity 泛型
- testkey
- 模板add,edit 统一

#### 2.2.0 `2021-01-25`
- 优化文件上传组件
- 升级springboot,mybatisplus,pageHelp的版本

#### 2.1 `2020-03-07`
- 提供配置固定获取登录用户的信息
- web拦截器允许OPTIONS类型的请求直接公国
- 更改的仓库的名称,地址也发生的变化
- 架构升级 spring-boot:2.x , mybatis-plus:3.3.1
- 去掉了 tablelog , springcloud 模块

#### 2.0.3 `2019-12-10`
- 删除和整理 mybatis 模块,只留下 mybatis-plus模块
- 优化 部分工具类

#### 2.0.2 `2019-06-21`
- 删除自己编写的excel工具类, 采用[alibaba/easyexcel](https://github.com/alibaba/easyexcel)

#### 2.0.1 `2019-06-21`
- 优化部分核心代码
- mybatis-plus继承实体接口的修改,BaseEntity类和IEntity接口对应两种不同的方式
- 优化druid的加载方式
```
# 关闭 druid 数据源连接池
sesame.framework.db.druid-enabled=false
```

#### 2.0.0 `2019-1-9`
- 整体目录发生大的变化,不兼容之前的版本
- 新增mybatis-plus 模块
- 本地缓存模块新增**framework-cache-local-caffeine**  [#详见文档](workspace/wjh/sesame-parent/adoc/ch4/local-redis.md)
- **优化** QueryCacha 注解 
```
修改参数名称
isWriteNullValue 改为 isWriteNullValueToJson
1. boolean isWriteNullValueToJson : 序列化时 , 是否写空值进json
新增参数
2. boolean isSaveNullValueToCache : 当查询结果为 null 时,是否写入缓存(redis)中,默认 false,一般不用设置
3. String saveNullValueTheStr : 当isSaveNullValueToCache=true 时, 在缓存(redis)中的值,默认"framework-cache-null"
```
- 依赖的spring boot 版本为2.0.7.RELEASE , spring cloud 版本为Finchley.SR2
- [升级修改事项](workspace/wjh/sesame-parent/adoc/spring-boot-2.x.md)

#### 1.3.1 `2018-5-8 `
1. 废弃 AbstractUserCache 这个空实现类,采用java8 的接口 default方法
2. 新增 websocket 模块,暂时提供了单机支持,集群需要自己去实现
3. 修改对称加密,优化线程安全 EncryptionAndDecryption.java
4. 新增Excel解析类 XLSXCovertCSVReader.java(framework-core)
5. 更改时间类型参数传到后台如果值为空,返回 new Date()问题,现在返回 null
6. framework-core 增加运行环境判断工具类,VifRunEnv.isJar(Class clazz);
7. framework-web 增加签名校验,动态tokean校验,时间控制,使用文档还没有写.懒一会
8. framework-cache-redis 增加多redis实例支持, [#详见文档](workspace/wjh/sesame-parent/adoc/ch4/redis-shili.md)
9. 新增 framework-rocketmq 模块, 参考[rocketmq/rocketmq-spring-boot-starter](https://github.com/rocketmq/rocketmq-spring-boot-starter) , [apache/rocketmq-externals](https://github.com/apache/rocketmq-externals/tree/master/rocketmq-spring-boot-starter)
10. 新增 framework-web-swagger, 用于开启swagger, 文档还没写
11. 新增 framework-tablelog 用于记录某些表,某些字段, 的修改记录, 文档还没写

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
