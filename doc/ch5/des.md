> 界面采用的技术
- layui 官网文档地址 : http://www.layui.com/doc
- 99%的前后分离,为什么说是99%呢? 因为所有的html界面都通过/page/*~*.html 转发,但页面上的所有数据都是通过ajax请求而来
- 这一节所有的文档都将是介绍我封装的东西的用法,layui的相关知识请到官网查看
- 这里以 sso 项目为例, [项目源码](https://gitee.com/sesamekim/sso-web)

> 项目结构
```
sso-web
|  |-src/main/java : java代码
|  |-src/main/resources
|  |   |-config : 项目配置文件
|  |   |-public : 静态文件夹,一般用来存放 index.html
|  |   |-static : 静态文件夹,一般用来存放静态资源, js, css 等
|  |   |   |-index : 首页样式,不允许修改
|  |   |   |-istyle : 项目的js,css 文件
|  |   |   |    |-css
|  |   |   |    |-js
|  |   |   |    |  |-common.js : 所有界面的公共js方法
|  |   |   |    |  |-data.js : 系统的 js 常量
|  |   |   |    |  |-footer.js : 所有界面渲染完成之后执行的js方法
|  |   |   |    |-layui-module : layui 的扩展模块
|  |   |   |    |  |-user.js : 用户模块,里面包含用户信息,角色信息获取的方法 
|  |   |   |    |  |-verify.js : from 表单的公共校验部分,layui 的扩展
|  |   |   |    |  |-web.js : 项目的工具模块,好像名字没取太规范啊!!
|  |   |   |    |  |-后面会有章节详细介绍这三个模块
|  |   |   |_templetes : 项目的所有html文件存放地
```