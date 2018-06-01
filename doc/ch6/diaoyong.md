> spring cloud 提供两种模式 ribbon 和 feign
- 生产者: producer [https://gitee.com/sesamekim/demo/tree/master/spring-cloud/producer](https://gitee.com/sesamekim/demo/tree/master/spring-cloud/producer)
- 消费者：comsumer [https://gitee.com/sesamekim/demo/tree/master/spring-cloud/comsumer](https://gitee.com/sesamekim/demo/tree/master/spring-cloud/comsumer)

##### 懒得写文档了,看示例代码把, 里面都有
- 启动 eureka
- 然后启动 producer和comsumer
- 然后访问 http://localhost:8060/comsumer/feign/getUser
- 项目中推荐使用 feign 模式
- 熔断(服务降级)请查看类 FeignServiceHystrix.java
