package kim.sesame.framework.cache.redis.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import kim.sesame.common.web.context.SpringContextUtil;
import kim.sesame.framework.cache.annotation.QueryCache;
import kim.sesame.framework.cache.redis.config.QueryCacheProperties;
import kim.sesame.framework.cache.redis.server.CacheServer;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * https://www.cnblogs.com/dreamfree/p/4102619.html
 */
@Aspect
@Component
@CommonsLog
public class QueryCacheAop {

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
        log.debug("");
        // 获取注解
        MethodSignature sign = (MethodSignature) pjd.getSignature();
        Method method = sign.getMethod();
        Class returnTypeClazz = method.getReturnType();// 方法返回类型
        QueryCache ann = method.getAnnotation(QueryCache.class);

        // 获取方法里的参数
        Object[] params = pjd.getArgs();
        String classPath = pjd.getSignature().getDeclaringTypeName();
        String methodName = pjd.getSignature().getName();

        String cacheKey = getCacheKey(ann.key(), ann.keyPrefix(), classPath, methodName, params);
        log.debug("缓存 : key = " + cacheKey);
        int time = 0;
        TimeUnit timeUnit = null;
        if (ann.invalidTime() <= 0) {
            time = queryCache.getInvalidTime();
            timeUnit = TimeUnit.MINUTES;
        } else {
            time = ann.invalidTime();
            timeUnit = ann.timeUnit();
        }
        String cacheResult = null;
        // 如果redis 异常，那么就直接执行目标方法
        try {
            cacheResult = stringRedisTemplate.opsForValue().get(cacheKey);
        } catch (Exception e) {
            log.debug("redis 获取异常，直接执行目标方法");
            e.printStackTrace();
            return pjd.proceed();
        }

        if (StringUtils.isEmpty(cacheResult)) {
            log.debug(MessageFormat.format("缓存不存在,走数据库查询 ,存储时间 : {0} , 单位 : {1} ", time, timeUnit));
            result = pjd.proceed();

            if (result != null) {
                String json = "";
                if (result instanceof String) {
                    json = result.toString();
                } else {
                    if (ann.isWriteNullValueToJson()) {
                        json = JSON.toJSONString(result, SerializerFeature.WriteMapNullValue, SerializerFeature.UseSingleQuotes);
                    } else {
                        json = JSON.toJSONString(result, SerializerFeature.UseSingleQuotes);
                    }
                }
                try {
                    stringRedisTemplate.opsForValue().set(cacheKey, json, time, timeUnit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // result==null
                if (ann.isSaveNullValueToCache()) {
                    String json = ann.saveNullValueTheStr(); // 给结果赋值,但是在获取的时候也要判断的
                    try {
                        stringRedisTemplate.opsForValue().set(cacheKey, json, time, timeUnit);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            // 1. 判断结果是不是最开始写入的null 字符串,如果是, 直接返回 null
            if (ann.isSaveNullValueToCache() && cacheResult.equals(ann.saveNullValueTheStr())) {
                return null;
            }

            // 2. 反序列化字符串,并返回对象
            // 返回类型为 list和Set 集合
            if (Collection.class.isAssignableFrom(returnTypeClazz)) {
                ResolvableType resolvableType = ResolvableType.forMethodReturnType(method);
                Class clazz = resolvableType.getGeneric(0).resolve();
                clazz = clazz == null ? Object.class : clazz;

                if (List.class.isAssignableFrom(returnTypeClazz)) {
                    result = JSONArray.parseArray(cacheResult, clazz);
                } else if (Set.class.isAssignableFrom(returnTypeClazz)) {
                    result = new HashSet<>(JSONArray.parseArray(cacheResult, clazz));
                } else {
                    throw new ClassCastException(MessageFormat.format
                            ("不支持的类型:{0},缓存只支持 List和Set 的集合类型", returnTypeClazz));
                }
            } else if (returnTypeClazz == String.class) {
                result = cacheResult;
            }
            // 判断返回类型 , 返回类型为单个对象
            else {
                result = JSON.parseObject(cacheResult, returnTypeClazz);
            }
        }
        log.debug(result);
        log.debug("");
        return result;
    }

    /**
     * 缓存key的算法,
     */
    private static String getCacheKey(String key, String prefix, String classPath, String methodName, Object... params) {
        StringBuffer sb = new StringBuffer();

        if (StringUtils.isNotEmpty(key)) {
            sb.append(key);
        } else {
            if (StringUtils.isNotEmpty(prefix)) {
                sb.append(prefix).append("_");
            }
            sb.append(classPath).append(".").append(methodName);
        }

        if (params != null && params.length > 0) {
            sb.append("_");
            for (Object s : params) {
                if (s != null) {
                    sb.append(s.toString()).append("#");
                }
            }
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    public static void invalid(String key, String prefix, String classPath, String methodName, Object[] params) {
        String cachekey = getCacheKey(key, prefix, classPath, methodName, params);

        CacheServer cacheServer = SpringContextUtil.getBean(CacheServer.class);
        cacheServer.invalid(cachekey);
    }

}
