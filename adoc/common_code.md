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

        Boolean trFlag = transactionTemplate.execute(status -> {
            try {

            } catch (Exception e) {
                log.error("逻辑操作失败 ",  e);
                status.setRollbackOnly();
                return false;
            }
            return true;
        });
        if (!trFlag) {
            throw new BizException("保存数据异常");
        }
```

####  list 分段执行
- [https://blog.csdn.net/CodersCoder/article/details/115396315](https://blog.csdn.net/CodersCoder/article/details/115396315)
```java
import com.google.common.collect.Lists;
```
```java
//假设查询出很多用户信息
List<User> users = userService.findAll();
//按每100个一组分割
List<List<User>> parts = Lists.partition(users, 100);
parts.stream().forEach(list -> {
    process(list);
});

## 分组

```

#### list 累加
```java
Integer quantity = list.stream().map(ClassName::getQuantity)
                        .filter(l -> l != null)
                        .reduce(0, Integer::sum);
```

#### list 分组 => Map
```java
Map<String, List<ClassName>> successMap = successList.stream()
            .collect(Collectors.groupingBy(ClassName::getKey));

Map<Long, Map<Integer, List<OrderItem>>> vendorOrderItemMapMap = orderItems.stream()
        .collect(Collectors.groupingBy(OrderItem::getVendorId, Collectors.groupingBy(OrderItem::getOrderType)));

    for (Long vendorId : vendorOrderItemMapMap.keySet()){
        Map<Integer, List<OrderItem>>vendorOrderItemMap=vendorOrderItemMapMap.get(vendorId);
        for(Integer orderType:vendorOrderItemMap.keySet()){
            List<OrderItem> tiems=vendorOrderItemMap.get(orderType);
        }
    }
```

#### list 求和
```java
Integer quantityTotal = request.stream().map(DistributionSkuImportDataRequest::getQuantity)
    .filter(l -> l != null)
    .map(Integer::parseInt)
    .reduce(0, Integer::sum);

BigDecimal totalSplitAmount = splitInfoList.stream()
    .map(PaymentSplitInfoVO::getSplitAmount)
    .reduce(BigDecimal.ZERO, BigDecimal::add);
```

#### 驼峰字符串转换
```java
import com.google.common.base.CaseFormat;
// 连接符转驼峰，首字母小写 结果testData
System.out.println(CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, "test-data"));

// 连接符转驼峰，首字母大写 结果TestData
System.out.println(CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, "test-data"));

// 下划线转驼峰，首字母小写 结果testData
System.out.println(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "test_data"));

// 下划线转驼峰，首字母大写 结果TestData
System.out.println(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, "test_data"));

// 驼峰转小写下划线 结果test_data
System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "testData"));

// 驼峰转大写下划线 结果TEST_DATA
System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, "TestData"));

// 驼峰转小写连接符 结果test-data
System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, "testData"));
```
