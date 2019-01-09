> 项目中配置了 redis, 但有时候可能会遇到要操作另外好几个redis的实例,(注意:这里不是集群,不是集群).但又不能影响默认的redisTemplate的使用.

####  jedisShard 方式实现 `分片`
- 支持集群分片,pwds 只写一个或为空,则表示所有的pwd都是同一个
- 里面的 aaaa 是这个jedis 配置在内存里的key, 如果只有一个,就可以随便写了,如果有多个,这个可以必须不一样
- 这个key 在后面使用时会用到,如果只有一个就可以忽略了
- 最简单的配置如下,如要查看详细配置,请查看源代码
```
sesame.framework.redis.shard.nodes.aaa.hosts=ip:prot,ip:prot
sesame.framework.redis.shard.nodes.aaa.pwds=pwd,pwd
```
- 用法
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