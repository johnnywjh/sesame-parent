> 虽然有了redis缓存, 但有时候一些特殊的场景还是需要本地缓存的,适当牺牲一点内存来换取速度(并发量或者计算速度),并且一般都需要有可以设置失效时间
关于spring-boot-starter-cache在springboot 1.x(对应springmvc4.x) 的版本里官方推荐的是guava , 但是在 2.x(对应springmvc5.x)的版本里去掉了guava,采用了caffeine
至于为什么呢? ==> [是什么让spring 5放弃了使用Guava Cache？](https://blog.csdn.net/qq_38398479/article/details/70578876)

## 本地缓存,并且可个性化设置每个注解的过期时间
1. 依赖
```
<dependency>
    <groupId>kim.sesame.framework</groupId>
    <artifactId>framework-cache-local-caffeine</artifactId>
</dependency>
```
2. 继承LocalTTlCacheAbstract , 实现 setConfigs()方法,并加入配置
```
import kim.sesame.framework.cache.local.caffeine.config.LocalTTLCacheEntity;
import kim.sesame.framework.cache.local.caffeine.config.LocalTTlCacheAbstract;
import org.springframework.stereotype.Component;

import java.util.Arrays;

  @Component  // 一定要加入spring容器中
  public class CacheImpl1 extends LocalTTlCacheAbstract {
      @Override
      protected void setConfigs() {
          configs = Arrays.asList(
                  new LocalTTLCacheEntity("userlist", 10)  // 第一个参数是 @Cacheable 注解的 value 值
          );
      }
  }
```
- 注意 : LocalTTLCacheEntity 这个类可以设置具体的过期时间和最大个数,有默认值

3. 具体业务代码中的使用
```
    @Cacheable(value = "userlist", key = "#id")
    public List<User> searchList(String id) {
        List<User> list = Arrays.asList(
                new User("1", "张三", 1, new Date())
                , new User("2", "李四", 2, new Date())
        );
        log.info("执行了 searchList 方法:" + id);
        return list;
    }
```



