##### 怎样使用?
```
<script>
layui.use(['user'], function () {
    var user = layui.user;

    //......
    console.log(user.getUser());// 打印登录后的用户信息
    
});    
</script>
```
###### 注意: 所有的模块都是按需加载,如果你的某个界面用不到这个模块,就不要去加载它

#### 功能方法描述
- getUser() : 登录用户的信息
- getRole() : 登录用户的角色信息
- initData() : 初始化用户/角色 信息到浏览器缓存中(会话数据库)
- checkAuth(obj) : 权限控制标签, 参数obj是权限的key
- cleanAuth() : 从缓存中清除权限数据,下一次使用时会重新加载(在登录成功后用到过这个方法)