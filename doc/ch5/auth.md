#### html 标签写法
```
<div authkey="..."></div>
```
- 在需要控制的标签上加上属性 authkey=""
- 在页面最后面加上 
```
<div th:include="../templates/aaacommon/basic::html_footer"></div>
```
, 里面有一段代码是 : user.checkAuth();
- 如果是加载表格后,表格渲染染成之后需要执行一下 user.checkAuth();
```
  $("#query_data").click(function () {
    web.load();
    web.page_query("#", "#", function (list) {
    table.render({
        elem: '#' + table_list_id
        , data: list
        // ........省略
    });
    user.checkAuth();
  });
});
```
- authkey="..." 的值,是 '系统编号_菜单编号'
- 属性里这四个值是默认就有的, 如果暂时不知道key是什么可以用这个,key_search,key_add,key_update,key_delete

##### 是不是很简单,哈哈哈哈哈