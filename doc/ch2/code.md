##### 这个工具是我为了偷懒而开发的,如果大家使用后有什么好的建议,欢迎大家提出来

##### 工具地址 [http://www.sesame.kim/code](http://www.sesame.kim/code)

#### 1 在首页里填好 数据库连接
#### 2 然后选择一个表,选择模板 [tpl-2.0]
![输入图片说明](https://gitee.com/uploads/images/2017/1224/180707_1de7d0a2_1599674.png "屏幕截图.png")

#### 3 点击提交,把生成的代码复制到项目里,如下图
![输入图片说明](https://gitee.com/uploads/images/2017/1224/181042_eb510ca5_1599674.png "屏幕截图.png")
- 我使用的是 thymeleaf 模板,这个是springboot 官方推荐的模板,比jsp强大太多太多
- 这里我封装了一点前端的东西,详情见 demo

#### 4 把项目运行起来 http://localhost:8081/ssm/page/student~student_list
- @SpringBootApplication注解上 添加@ServletComponentScan ,这个是项目启动时,初始化一些值到内存里面
- 界面如果不出来,根据错误修改 html
- 前端采用 layui , 官网地址 http://www.layui.com
- 成功的界面展示如下,点击查询 
![输入图片说明](https://gitee.com/uploads/images/2017/1224/185156_ae250db6_1599674.png "屏幕截图.png")

#### 5 代码解释
- 请大家不要过度依赖代码生成工具,建议手写 建议手写 建议手写
- 部分代码我有修改,详情 **ssm代码地址 [https://gitee.com/sesamekim/demo/tree/master/ssm](https://gitee.com/sesamekim/demo/tree/master/ssm)**
- 分页查询代码, 这里不解释为什么,不熟悉的请到 [PageHelper ](https://gitee.com/free/Mybatis_PageHelper)
 官网学习
```
	@RequestMapping("/listPage")
	public Response listPage(Student bean, GPage gPage) {

		Page<Student> pages = PageHelper.startPage(gPage.getPageNum(), gPage.getPageSize())
                        .doSelectPage(() -> studentService.searchList(bean));
        List<Student> list = pages.getResult();
		
		return returnSuccess(list, PageUtil.recount(gPage, pages));
	}
```
- 可以尝试在业务层抛异常,可以在界面上直接显示出来
![输入图片说明](https://gitee.com/uploads/images/2017/1224/190254_782ade23_1599674.png "屏幕截图.png")
![输入图片说明](https://gitee.com/uploads/images/2017/1224/190347_4cb0748c_1599674.png "屏幕截图.png")
- 未完待续, 不定期更新