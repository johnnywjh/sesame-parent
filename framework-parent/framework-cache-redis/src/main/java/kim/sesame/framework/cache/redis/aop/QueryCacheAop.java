package kim.sesame.framework.cache.redis.aop;

import kim.sesame.framework.cache.annotation.QueryCache;
import kim.sesame.framework.cache.annotation.ResultType;
import kim.sesame.framework.cache.redis.config.QueryCacheProperties;
import kim.sesame.framework.cache.redis.server.CacheServer;
import kim.sesame.framework.utils.GsonUtil;
import kim.sesame.framework.utils.StringUtil;
import kim.sesame.framework.web.context.SpringContextUtil;
import lombok.extern.apachecommons.CommonsLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * https://www.cnblogs.com/dreamfree/p/4102619.html
 */
@Aspect
@Component
@CommonsLog
public class QueryCacheAop {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    QueryCacheProperties queryCache;

    /**
     * 环绕通知
     */
    @Around("@annotation(kim.sesame.framework.cache.annotation.QueryCache)")
    public Object aroundMethod(ProceedingJoinPoint pjd) throws Throwable {
        Object result = null;

        // 获取注解
        MethodSignature sign = (MethodSignature) pjd.getSignature();
        Method method = sign.getMethod();
        QueryCache ann = method.getAnnotation(QueryCache.class);

        // 获取方法里的参数
        Object[] params = pjd.getArgs();
        String classPath = pjd.getSignature().getDeclaringTypeName();
        String methodName = pjd.getSignature().getName();

        String cacheKey = getCacheKey(ann.keyPrefix(), classPath, methodName, params);
        log.debug("缓存 : key = " + cacheKey);
        int time = 0;
        TimeUnit timeUnit = null;
        if (ann.invalidTime() == 0) {
            time = queryCache.getInvalidTime();
            timeUnit = TimeUnit.MINUTES;
        } else {
            time = ann.invalidTime();
            timeUnit = ann.timeUnit();
        }
        // 判断返回类型 , 返回类型为单个对象
        if (ann.resultType() == ResultType.Object) {
            Object cacheResult = redisTemplate.opsForValue().get(cacheKey);
            if (cacheResult == null) {
                log.debug(MessageFormat.format("缓存不存在,走数据库查询 ,存储时间 : {0} , 单位 : {1} ", time, timeUnit));
                result = pjd.proceed();
                redisTemplate.opsForValue().set(cacheKey, result, time, timeUnit);
            } else {
                result = cacheResult;
            }
        }
        // 返回类型为 list 集合
        else if (ann.resultType() == ResultType.List) {
            String cacheResult = stringRedisTemplate.opsForValue().get(cacheKey);
            if (StringUtil.isEmpty(cacheResult)) {
                log.debug(MessageFormat.format("缓存不存在,走数据库查询 ,存储时间 : {0} , 单位 : {1} ", time, timeUnit));
                result = pjd.proceed();
                String json = GsonUtil.getGson().toJson(result);
                stringRedisTemplate.opsForValue().set(cacheKey, json, time, timeUnit);
            } else {
                result = GsonUtil.fromJsonList(cacheResult, ann.resultClazz());
            }
        }
        log.debug(result);
        return result;
    }

    /**
     * 缓存key的算法,
     */
    private static String getCacheKey(String prefix, String classPath, String methodName, Object... params) {
        StringBuffer sb = new StringBuffer();

        if (StringUtil.isNotEmpty(prefix)) {
            sb.append(prefix).append("_");
        }
        sb.append(classPath).append(".").append(methodName);

        if (params != null && params.length > 0) {
            sb.append("_");
            for (Object s : params) {
                if (s != null) {
                    sb.append(s.toString()).append("#");
                }
            }
        }
        return sb.toString().substring(0, sb.toString().length() - 2);
    }

    public static void invalid(String prefix, String classPath, String methodName, Object[] params) {
        String key = getCacheKey(prefix, classPath, methodName, params);

        CacheServer cacheServer = SpringContextUtil.getBean(CacheServer.class);
        cacheServer.invalid(key);
    }

}
