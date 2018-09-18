### 环境
- jdk 1.8
- spring boot 1.5.13
- tool : idea (强烈建议)
- 什么? 你还不熟悉 idea, 这可是号称当下最流行的java开发工具,**[帮助文档](https://github.com/judasn/IntelliJ-IDEA-Tutorial) [Window快捷键](https://github.com/judasn/IntelliJ-IDEA-Tutorial/blob/master/keymap-introduce.md) [Mac快捷键](https://github.com/judasn/IntelliJ-IDEA-Tutorial/blob/master/keymap-mac-introduce.md)** 

### 项目子模块描述
<table>
    <tr>
        <th colspan="2">架构模块 framework-parent</th>
    </tr>
    <tr>
        <td>framework-cache</td>
        <td>缓存中定义,一般是接口和常量</td>
    </tr>
    <tr>
        <td>framework-cache-redis</td>
        <td>cache 的 redis 实现</td>
    </tr>
    <tr>
        <td>framework-core</td>
        <td>所有模块的基础模块</td>
    </tr>
    <tr>
        <td>framework-distributed-tx</td>
        <td>分布式事物</td>
    </tr>
    <tr>
        <td>framework-email</td>
        <td>简单邮件,模板邮件, 发送</td>
    </tr>
    <tr>
        <td>framework-lock</td>
        <td>分布式锁</td>
    </tr>
    <tr>
        <td>framework-lock-serial</td>
        <td>分布式序列号的生成</td>
    </tr>
    <tr>
        <td>framework-mybatis</td>
        <td>mybatis的基础封装</td>
    </tr>
    <tr>
        <td>framework-rocketmq</td>
        <td>rocketmq的一些操作</td>
    </tr>
    <tr>
        <td>framework-spring-boot</td>
        <td>springcloud 的封装,包含cloud,starter模块</td>
    </tr>
    <tr>
        <td>framework-tablelog</td>
        <td>记录数据库表的某些字段的修改记录,可配置</td>
    </tr>
    <tr>
        <td>framework-web</td>
        <td>web模块的封装</td>
    </tr>
    <tr>
        <td>framework-utils</td>
        <td>
            <p>framework-utils-accessory : 附件上传模块</p>
            <p>framework-utils-swagger : swagger的一些封装</p>
        </td>
    </tr>
    <tr>
        <td>framework-websocket</td>
        <td>websocket推送---单机版</td>
    </tr>
    <tr>
        <td>framework-websocket-cloud</td>
        <td>websocket推送--集群版---未完成</td>
    </tr>
</table>

<table>
    <tr>
        <th colspan="2">maven插件 sesame-plugins</th>
    </tr>
    <tr>
        <td>reload-resources-file</td>
        <td>重新加载资源文件,用于html界面编写后的编译到target目录</td>
    </tr>
    <tr>
        <td>zip</td>
        <td>指定目录文件的压缩</td>
    </tr>
</table>
