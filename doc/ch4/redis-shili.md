> 项目中配置了 redis, 但有时候可能会遇到要操作另外好几个redis的实例,(注意:这里不是集群,不是集群).但又不能影响默认的redisTemplate的使用.
#### 现在支持两种方式
#### 方式一. jedisShard 方式实现 `一般情况下推荐这种`
- 支持集群分片,pwds 只写一个或为空,则表示所有的pwd都是同一个
- 里面的 aaaa 是这个jedis 配置在内存里的key, 如果只有一个,就可以随便写了,如果有多个,这个可以必须不一样
- 这个key 在后面使用时会用到,如果只有一个就可以忽略了
- 最简单的配置如下,如要查看详细配置,请查看源代码
```
sesame.framework.shard.nodes.aaa.hosts=ip:prot,ip:prot
sesame.framework.shard.nodes.aaa.pwds=pwd,pwd
```
- 用法
- 如果只有一个配置
```
// 1 普通存值
String res = JedisShardService.set(key, value, 2, TimeUnit.MINUTES);
String value1 = JedisShardService.get(key);

// 2 复杂操作
Long t = JedisShardService.op(mapKey, (jedis) -> {
            return jedis.lpush(key, value.split(","));
        }, Long.class);
// 说明 : mapKey 就是上面提到的key, 在只有一个的时候可以不写, 但是多个的时候必须写
```
##### 更详细的操作见[源码地址](../../framework-parent/framework-cache-redis/src/main/java/kim/sesame/framework/cache/redis/shard) 他们说:经常读源码是一个好习惯


#### 方式二. redisTemplate 方式
- 暂时只支持单机的,因为舍不得删代码所有就保留下来了,怎么集群还在思考中.....
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

##### 更详细的操作见[源码地址](../../framework-parent/framework-cache-redis/src/main/java/kim/sesame/framework/cache/redis/dynamic) 他们说:经常读源码是一个好习惯