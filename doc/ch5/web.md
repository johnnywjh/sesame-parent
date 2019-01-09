##### 怎样使用?
```
<script>
layui.use(['sysweb'], function () {
    var sysweb = layui.sysweb;

    //......
    sysweb.req({
        url:''
        ,data:{}
        ,done:function(d){}
    });
    
});    
</script>
```
###### 注意: 所有的模块都是按需加载,如果你的某个界面用不到这个模块,就不要去加载它

#### 功能方法描述`实际以源码为主`
- url_replace(url) : 返回处理之后的url
- ajax(url, type, data, successfn) :异步请求,你们封装了异常的处理,使用后只需操心成功后的返回
- ajax_sync(url, type, data, successfn) : 同步请求 : 会锁住页面==> 页面下面的 javascript 必须等待回调函数处理完 才能执行
- page_query(form_id, page_id, refreshdatafn) : 分页查询 form_id:表单的id,用户获取请求的参数,url,有默认值. page_id:分页信息展示的div.id,有默认值. successfn: 请求数据成功之后的回调,需要编写表格的渲染,如果有权限控制需要加上user.checkAuth();
```
/**
 *  list 界面上的删除
 * @param contro_path  controller 的路径,
 * @param ids  删除的id  类似 1,2,3
 * @param delfn 删除成功后的回调
 */
 , delete_select: function (contro_path, ids, delfn)
```
```
/**
 *  list 界面上的全部删除
 * @param contro_path  controller 的路径,
 * @param table_list_id   table 的id , 用于获取选中的行
 * @param delfn  删除成功后的回调
 */
 , delete_select_all: function (contro_path, table_list_id, delfn)
```