> 现在流行微服务,然后就会有分布式锁的需求,这个有两个实现
- framework-distributed-lock : 别人的,我抄过来的,嘿嘿 这里不讲解使用
- framework-lock : 这个是我封装的,参考 **[redis 官网](https://github.com/redisson/redisson/wiki/%E7%9B%AE%E5%BD%95)**
#### 1 pom.xml 添加依赖
```
<dependency>
    <groupId>${sesame.groupId}</groupId>
    <artifactId>framework-lock</artifactId>
</dependency>
```
#### 2 添加如下配置
```
redisson.address=redis://localhost:6379
redisson.password=123456
```

#### 3 新建一个类
```
import kim.sesame.framework.lock.service.DistributedLocker;
import kim.sesame.framework.web.controller.AbstractWebController;
import kim.sesame.framework.web.response.Response;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@CommonsLog
@RestController
@RequestMapping("/test")
public class TestController extends AbstractWebController {

    @Autowired
    DistributedLocker locker;

    @RequestMapping("/readDb")
    public Response readDb() {
        String key = "redis_key_test";
        String name = Thread.currentThread().getName();

        log.info(MessageFormat.format("{0}尝试获取锁", name));
        try {
            locker.lock(key);
            log.info(MessageFormat.format("{0}获取锁成功", name));
            TimeUnit.SECONDS.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            locker.unlock(key);
            log.info(MessageFormat.format("{0} unlock success", name));
        }
        return returnSuccess();
    }
}

```
- 然后写一个测试的 main 方法
```
 public static void main(String[] args) {
        String url = "http://localhost:8083/lock/test/readDb";
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                HttpRequest.get(url).body();
            }).start();
        }
    }
```
执行结果
![输入图片说明](https://gitee.com/uploads/images/2017/1225/145120_ab263630_1599674.png "屏幕截图.png")

### lock 代码地址 [https://gitee.com/sesamekim/demo/tree/master/lock](https://gitee.com/sesamekim/demo/tree/master/lock)
