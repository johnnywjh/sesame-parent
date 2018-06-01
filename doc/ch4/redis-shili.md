> 项目中配置了 redis, 但有时候可能会遇到要操作另外好几个redis的实例,(注意:这里不是集群,不是集群).但又不能影响默认的redisTemplate的使用.
#### 配置
```
# 这个是最简单的配置,也是必须要配置的两个属性.
# 这里面的 aaaa 可以随意的写,它是一个 key,在后面使用的时候会用到

sesame.framework.redis.nodes.aaaa.host-name=
sesame.framework.redis.nodes.aaaa.password=
```

#### 使用
```
@RestController
public class Test extends AbstractWebController {

    static String key = "aaaaaaaaaaaaaaaaaaaaaaaaa";
    static int timeout = 2;
    static TimeUnit unit = TimeUnit.MINUTES;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @IgnoreLoginCheck
    @RequestMapping("/test1")
    public Response test1(String code) {
        stringRedisTemplate.opsForValue().set(key, code, timeout, unit);
        return returnSuccess();
    }

    @IgnoreLoginCheck
    @RequestMapping("/test2")
    public Response test2(String code) {
        DynamicRedisService.getStringRedisTemplate("aaaa").opsForValue()
                .set(key, code, timeout, unit);
        return returnSuccess();
    }
}
```

##### [源码地址](https://gitee.com/sesamekim/framework-boot/tree/master/framework-parent/framework-cache-redis/src/main/java/kim/sesame/framework/cache/redis/dynamic)