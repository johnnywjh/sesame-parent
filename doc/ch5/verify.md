##### 怎样使用?,这个模块只需要引入就可以了 

```
 <input type="text" name="account" lay-verify="charCode" placeholder="请输入账号,唯一" class="layui-input"/>

<script>
layui.use(['verify'], function () {
    
    
});    
</script>
```
######  使用的话和layui 表单校验一样( lay-verify="xxxx" ),只是增加了一些 选项
##### [文档地址](http://www.layui.com/doc/modules/form.html#verify)
 ###### 注意: 所有的模块都是按需加载,如果你的某个界面用不到这个模块,就不要去加载它

#### 选项
- charCode : 不能为空和有特殊字符,首尾不能出现下划线'_',不能全为数字
- aePhone : 手机号码校验 , 允许为空
- aeEmail : 邮箱校验 , 允许为空
- aeUrl : 链接格式校验 , 允许为空
- aeIdentity : 身份证号校验 , 允许为空
- aeNumber : 数字校验 , 允许为空
##### 是不是发现了什么规律? ae 就是 allowed empty
- pwd : 请输入8-16位数字和字母的组合
- comfirmPwd : 两次密码不一样, 和上一个文本框做比较
- ratio : 比例 (0-100的整数)
- money : 金额的规则