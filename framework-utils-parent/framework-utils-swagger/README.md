### swagger使用
> 参考文档
- https://doc.xiaominfo.com/docs/quick-start
- https://gitee.com/xiaoym/knife4j

#### 配置
```yaml
knife4j:
  enable: true
  openapi:
    title: Knife4j官方文档
    description: "`我是测试`,**你知道吗**
    # aaa"
    email: xiaoymin@foxmail.com
    concat: 八一菜刀
    url: https://docs.xiaominfo.com
    version: v4.0
    license: Apache 2.0
    license-url: https://stackoverflow.com/
    terms-of-service-url: https://stackoverflow.com/
    group:
      test1:
        group-name: 分组名称
        api-rule: package
        api-rule-resources:
          - com.knife4j.demo.new3
```

```
knife4j:
  enable: true
  openapi:
    title: 支付系统
    version: v1.0
    group:
      pay:
        group-name: 分组名称
        api-rule: package
        api-rule-resources:
          - com.rz.pay.api.controller
```
 