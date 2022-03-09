package kim.sesame.framework.cache.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 缓存注解
 * 定时失效缓存
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface QueryCache {

    /**
     * 完整的key, 如果有值,就不进行key的计算
     *
     * @return
     */
    String key() default "";

    /**
     * 缓存key 的前缀,一般加上项目名称
     *
     * @return keyPrefix
     */
    String keyPrefix() default "";

    /**
     * 缓存失效时间类型,默认分钟
     *
     * @return timeUnit
     */
    TimeUnit timeUnit() default TimeUnit.MINUTES;

    /**
     * 缓存失效时间, 默认30
     *
     * @return invalidTime
     */
    int invalidTime() default 0;

    /**
     * 序列化时 , 是否写空值进json
     *
     * @return
     */
    boolean isWriteNullValueToJson() default true;

    /**
     * 当查询结果为 null 时,是否写入缓存(redis)中
     * 默认 false,一般不用设置
     *
     * @return
     */
    boolean isSaveNullValueToCache() default false;

    /**
     * isSaveNullValueToCache=true 时, 在缓存(redis)中的值
     * 默认有值, 一般不用设置,有冲突时才自定义设置,并且只有在isSaveNullValueToCache=true 时才有意义
     *
     * @return
     */
    String saveNullValueTheStr() default "framework-cache-null";
}
