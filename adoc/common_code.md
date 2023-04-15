#### 多配置
```yaml
spring:
  profiles:
    active: local
---
spring:
  cloud:
    nacos:
      config:
        server-addr: xxxx
  config:
    activate:
      on-profile: DEV
---
spring:
  cloud:
    nacos:
      config:
        server-addr: yyyy
  config:
    activate:
      on-profile: test
```
#### 返回给前端Long=>String
```java
// 方式一
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@JsonSerialize(using = ToStringSerializer.class)
private Long id;
```

```java
// 方式二
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
private Long id;
```

#### 编程式事务
```java
    @Autowired
    private TransactionTemplate transactionTemplate;

    public void demoTestService(TestTransactionReq req) {

        transactionTemplate.execute(status -> {
            try {
                boolean res = demoTestRepository.update(new LambdaUpdateWrapper<DemoTestPo>()
                        .eq(DemoTestPo::getId, req.getId())
                        .set(DemoTestPo::getName, req.getName())
                );
                if (!res) {
                    log.info(">>> id is not null");
                }
                if ("1".equals(req.getName())) {
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 这个会抛异常
                    status.setRollbackOnly();
                    log.info(">>> 回滚事物-1");
                    return false;  // 这个不会导致事务回滚
                } else if ("2".equals(req.getName())) {
                    throw new BizException("模拟异常");
                }
            } catch (Exception e) {
                log.info(">>> 回滚事物-2");
                status.setRollbackOnly();
                return false;
            }
            return true;
        });
    }
```

####  list 分组
- [https://blog.csdn.net/CodersCoder/article/details/115396315](https://blog.csdn.net/CodersCoder/article/details/115396315)
```java
import com.google.common.collect.Lists;

//假设查询出很多用户信息
List<User> users = userService.findAll();
//按每100个一组分割
List<List<User>> parts = Lists.partition(users, 100);
parts.stream().forEach(list -> {
    process(list);
});
```
