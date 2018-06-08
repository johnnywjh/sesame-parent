1. 定时失效缓存 : 一般用于一些频率很高的查询, 支持Object 和 List<T> 返回类型
2. 自动刷新缓存 : 一般用于只读数据,提高查询速度


#### 准备工作
- pom.xml 加入
```
 <dependency>
    <groupId>${sesame.groupId}</groupId>
    <artifactId>framework-cache-redis</artifactId>
</dependency>
```
- 配置文件加入
```
# session 配置
spring.redis.host=localhost
# spring.redis.port=6379
spring.redis.password=

# 定时失效缓存 失效时间, 默认10分钟
sesame.framework.cache.invalid-time=10
# 自动缓存 失效时间, 默认60分钟
sesame.framework.cache.refresh-time=60
```

#### 1 定时失效缓存
- 在目标方法上加上 @QueryCache
- 注意:这个方法的调用者必须是 spring 管理的对象,如果用 this.queryDetail,是无法生效的,如果实在是要内部调用,请参考下面的写法
- SpringContextUtil.getBean(StudentService.class).queryDetail(id, name);
- 这个方法里的参数列表 不要使用对象,尽量使用基本数据类型,因为它参与 cacheKey
的算法
```
// 添加到缓存
    @QueryCache
    public Student queryDetail(String id, String name) {
        Student bean = new Student();
        bean.setId(id);
        return studentDao.search(bean);
    }

// 缓存失效 --> 一般在缓存方法后面写上 方法名规则如下
// 这里需要注意缓存key的运算方式
    @QueryCache
    public Student queryDetail_invalid(String id, String name) {
        String classPath = this.getClass().toString().replace("class ", "");
        QueryCacheAop.invalid(null, null, classPath, "queryDetail", new Object[]{id,name});
    }

```
##### 注解里的参数
1. String **key** : 无值,cacheKey=classPath+methodName+params . 有值,cacheKey=key+params **无特殊要求一般不用设置** 
2. String **keyPrefix** : 缓存key 的前缀,一般加上项目名 **无特殊要求一般不用设置** 
3. TimeUnit **timeUnit** : 缓存失效时间类型,默认分钟  **无特殊要求一般不用设置** 
4. int **invalidTime** : 缓存失效时间, 默认30 **无特殊要求一般不用设置** 
7. boolean **isWriteNullValue** : 序列化时 , 是否写空值进去


#### ------------------------- 这是分割线  --------------------------------------------------

#### 2 自动刷新缓存
- 新建一个类 CacheStudentServer 继承 AutoRefreshCache<T> 类
```
import com.sesame.student.bean.Student;
import com.sesame.student.dao.StudentDao;
import kim.sesame.framework.cache.redis.config.QueryCacheProperties;
import kim.sesame.framework.cache.redis.server.AutoRefreshCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CacheStudentServer extends AutoRefreshCache<Student> {

    @Autowired
    private QueryCacheProperties queryCacheProperties;


    @Autowired
    private void init(StringRedisTemplate stringRedisTemplate) {
        this.clazz = Student.class;
        this.stringRedisTemplate = stringRedisTemplate;

        this.refreshTime = queryCacheProperties.getRefreshTime();
        // this.refreshTime = 1; // 这里可以修改刷新时间
    }

    @SuppressWarnings("all")
    @Autowired
    private StudentDao studentDao;

    @Override
    protected List<Student> dataLoading() {
        return studentDao.selectList(new Student());
    }

```
- 调用方式, 直接调用 getData() 方法
```
    @Autowired
    CacheStudentServer cacheStudentServer;

    public List<Student> searchList(Student bean) {

        List<Student> list = cacheStudentServer.getData();

        return list;
    }
```
#### 说明
- 需要重写 dataLoading() 方法,刷新是的数据来源
- init 方法一定要有,需要注入 StringRedisTemplate
- 在init 方法中可以设置 this.openCache=false;这样的话这个刷新线程就不会开启,getData()方法也就不会有数据,,,,聪明机制的你也许已经发现了, 在这个类里是可以写有 @QueryCache 的方法的


##### 结束,结束,结束
##### cache demo地址 : **[https://gitee.com/sesamekim/demo/tree/master/cache](https://gitee.com/sesamekim/demo/tree/master/cache)**

