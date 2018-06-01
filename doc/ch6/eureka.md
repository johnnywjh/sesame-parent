> spring cloud 有两种注册中心 Eureka 和 Consul , 老规矩,这里不讲原理,只说怎么用,也只介绍 Eureka

##### 1 pom.xml 添加依赖
```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-eureka-server</artifactId>
</dependency>
```

##### 2 配置
```
spring.application.name=eureka-server
server.port=2001

eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false

eureka.instance.hostname=localhost
eureka.client.serviceUrl.defaultZone=http://localhost:${server.port}/eureka/
```

##### 3 启动类
```
package com.sesame;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class AppEureka {
	public static void main(String[] args) {
		new SpringApplicationBuilder(AppEureka.class).web(true).run(args);
	}
}
```

##### 4 执行效果
![输入图片说明](https://gitee.com/uploads/images/2017/1226/104116_24b4667a_1599674.png "屏幕截图.png")

##### 5 eureka 也可实现高可用的配置,集群
- 注册中心可以相互注册,并实现收集到的服务共享
application-prod1.properties
```
# prod config 采用高可用 分布式注册中心
server.port=8091

eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true

eureka.instance.hostname=prod1
eureka.client.serviceUrl.defaultZone=http://localhost:8092/eureka/
```

application-prod2.properties
```
# prod config 采用高可用 分布式注册中心
server.port=8092

eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true

eureka.instance.hostname=prod2
eureka.client.serviceUrl.defaultZone=http://localhost:8091/eureka/
```

###### ----------------
- eureka 地址 [https://gitee.com/sesamekim/demo/tree/master/spring-cloud/eureka](https://gitee.com/sesamekim/demo/tree/master/spring-cloud/eureka)
- 参考资料 [Spring-Cloud基础教程](http://blog.didispace.com/Spring-Cloud%E5%9F%BA%E7%A1%80%E6%95%99%E7%A8%8B/)