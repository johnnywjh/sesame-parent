### 环境
- jdk 1.8
- spring boot 1.5.10
- tool : idea (强烈建议)
- 什么? 你还不熟悉 idea, 这可是号称当下最流行的java开发工具,**[帮助文档](https://github.com/judasn/IntelliJ-IDEA-Tutorial) [Window快捷键](https://github.com/judasn/IntelliJ-IDEA-Tutorial/blob/master/keymap-introduce.md) [Mac快捷键](https://github.com/judasn/IntelliJ-IDEA-Tutorial/blob/master/keymap-mac-introduce.md)** 

### 项目子模块描述
```
    <modules>
        <module>framework-core</module> <!--核心模块,其他模块大部分要依赖这个模块-->
        <module>framework-mybatis</module><!--mybatis 模块-->
        <module>framework-web</module><!--web 模块-->

        <module>framework-cache</module><!--缓存接口的定义-->
        <module>framework-cache-redis</module><!--缓存接口的实现-->

        <module>framework-lock</module><!--分布式锁 -->
        <module>framework-lock-serial</module><!--分布式序列,依赖 [ framework-lock ] -->

        <module>framework-distributed-tx</module><!--分布式事物配置 -->

        <module>framework-spring-boot</module><!--spring-boot 的一些集成 -->
    </modules>
```
