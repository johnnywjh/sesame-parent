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
    int invalidTime() default 30;

    /**
     * 目标方法返回值类型
     *
     * @return resultType
     */
    ResultType resultType() default ResultType.Object;

    /**
     * 当resultType = ResultType.List 时, List 里的类型
     * 反序列化时会用到
     *
     * @return resultClazz
     */
    Class resultClazz() default Object.class;
}
