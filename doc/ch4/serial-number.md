> 在项目中我们会经常遇到 生成订单号类似的需求,当服务集群后,可能会有重复的情况,虽然概率很低,但还是有可能会发生,另外,我们也要提高编码的可读性.(这里会用到分布式锁)
#### 1. pom.xml
```
        <dependency>
            <groupId>${sesame.groupId}</groupId>
            <artifactId>framework-web</artifactId>
        </dependency>
        <dependency>
            <groupId>${sesame.groupId}</groupId>
            <artifactId>framework-mybatis</artifactId>
        </dependency>
        <dependency>
            <groupId>${sesame.groupId}</groupId>
            <artifactId>framework-cache-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>${sesame.groupId}</groupId>
            <artifactId>framework-lock</artifactId>
        </dependency>
        <dependency>
            <groupId>${sesame.groupId}</groupId>
            <artifactId>framework-lock-serial</artifactId>
        </dependency>
```

#### 2. 配置文件中加入 redis信息
```
spring.datasource.url=jdbc:mysql://www.sesame.kim:3306/test?useUnicode=true&characterEncoding=UTF-8
spring.datasource.username=testacc
spring.datasource.password=123456

spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password=123456

redisson.address=redis://${spring.redis.host}:${spring.redis.port}
redisson.password=${spring.redis.password}
```

#### 3. 新建一个类 MySerialNumber 继承 SerialNumberRule
```
package com.sesame.config;

import kim.sesame.framework.serial.define.SerialNumberRule;

/**
 * 序列号配置类,
 * 里面的code要保证系统唯一
 */
public class MySerialNumber extends SerialNumberRule {

    public static final SerialNumberRule QAS_HEADLESS_CODE =new MySerialNumber
            ("微信", "WX", false, true, "yyMMdd",
                    false, "", true, "QAW", true,
                    true, 6, false, "");

    public static final SerialNumberRule QAS_ARBITRATION_CODE =new MySerialNumber
            ("支付宝", "ZFB", false, true, "yyMMdd",
                    false, "", true, "QAZ", true,
                    true, 6, false, "");


    private MySerialNumber(String name, String code, boolean needParams, boolean needTime, String timeFormat, boolean needDelimiter, String delimiter, boolean needLetterPrefix, String letterPrefix, boolean needNumber, boolean fixedNumLen, int numLen, boolean needLetterSuffix, String letterSuffix) {
        super(name, code, needParams, needTime, timeFormat, needDelimiter, delimiter, needLetterPrefix, letterPrefix, needNumber, fixedNumLen, numLen, needLetterSuffix, letterSuffix);
    }
}
```
- 注意 : 里面的code要全系统唯一
- 生成规则 SerialNumberRule 这个类里面有详细的注释
#### 4 TestController.java
```
package com.sesame.controller;

import kim.sesame.config.MySerialNumber;
import kim.sesame.framework.lock.service.DistributedLocker;
import kim.sesame.framework.serial.define.ISerialNumberService;
import kim.sesame.framework.web.controller.AbstractWebController;
import kim.sesame.framework.web.response.Response;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@CommonsLog
@RestController
@RequestMapping("/test")
public class TestController extends AbstractWebController {

    @Autowired
    private DistributedLocker locker;
    @Autowired
    private ISerialNumberService serialNumberService;


    @RequestMapping("/getNo1")
    public Response getNo1() {

        String no1 = serialNumberService.generateSerialNumber(MySerialNumber.QAS_HEADLESS_CODE);
        log.info("no1:" + no1);

        return returnSuccess(Arrays.asList(no1));
    }

    @RequestMapping("/getNo2")
    public Response getNo2() {

        String no2 = serialNumberService.generateSerialNumber(MySerialNumber.QAS_ARBITRATION_CODE);
        log.info("no2:" + no2);

        return returnSuccess(Arrays.asList(no2));
    }


}

```
#### 5 测试类
```
package com.sesame.controller;

import com.github.kevinsawicki.http.HttpRequest;

public class Test {
    public static void main(String[] args) {
        String url1 = "http://localhost:8084/serial/test/getNo1";
        String url2 = "http://localhost:8084/serial/test/getNo2";
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                HttpRequest.get(url1).body();
                sleep();
            }).start();
            new Thread(() -> {
                HttpRequest.get(url2).body();
                sleep();
            }).start();
        }
    }
    public static void sleep(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

```
#### 6 执行结果

![输入图片说明](https://gitee.com/uploads/images/2017/1225/155404_87949d57_1599674.png "屏幕截图.png")
- 从图中可以看到,不同的类别之间相互不影响,然后多线程下 相同的类型会一个个的走
- 最后最后,可以去关心一下数据库,它是这样的
![输入图片说明](https://gitee.com/uploads/images/2017/1225/155612_39b2f944_1599674.png "屏幕截图.png")

#### 代码地址 [https://gitee.com/sesamekim/demo/tree/master/serial-no](https://gitee.com/sesamekim/demo/tree/master/serial-no)