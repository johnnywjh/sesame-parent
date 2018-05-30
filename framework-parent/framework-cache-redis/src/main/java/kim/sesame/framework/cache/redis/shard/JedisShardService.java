package kim.sesame.framework.cache.redis.shard;

import kim.sesame.framework.utils.StringUtil;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class JedisShardService {
    //    static final String DEFAULT_MAP_KEY = "jedis_node_default_key";
    static Map<String, ShardedJedisPool> jedisPoolMap = new HashMap<>();
    static ShardedJedisPool jedisPool = null;

    private static void vifNull() {
        if (jedisPool == null) {
            throw new NullPointerException("当前存在至少两个配置,请传入mapkey, 或者sesame.framework.shard.nodes. 只配置一个key");
        }
    }
    /*
     *--------------------  操作默认的jedisPool start  --------------------------------------------------
     */

    /**
     * jedis 操作
     */
    public static <R> R op(Function<ShardedJedis, Object> f, Class<R> clazz) {
        return op(null, f, clazz);
    }

    /**
     * jedis 操作
     */
    public static Object op(Function<ShardedJedis, Object> f) {
        return op(null, f);
    }

    /**
     * 向 redis 存值
     *
     * @param key                 key
     * @param value               value
     * @param time                过期时间的值
     * @param timeUnit:过期时间单位,只接受 SECONDS,MINUTES,HOURS,DAYS , 默认SECONDS
     */
    public static String set(String key, String value, long time, TimeUnit timeUnit) {
        return set(null, key, value, time, timeUnit);
    }

    /**
     * Set the string value as value of the key. The string can't be longer than 1073741824 bytes (1
     * GB).
     *
     * @param key
     * @param value
     * @param nxxx  NX|XX, NX -- Only set the key if it does not already exist. XX -- Only set the key
     *              if it already exist.
     * @param expx  EX|PX, expire time units: EX = seconds; PX = milliseconds
     * @param time  expire time in the units of <code>expx</code>
     * @return Status code reply
     */
    public static String set(String key, String value, String nxxx, String expx, long time) {
        return set(null, key, value, nxxx, expx, time);
    }

    /**
     * 返回 key 对应的值
     */
    public static String get(String key) {
        return get(null, key);
    }
    /*
     *--------------------  操作默认的jedisPool end  --------------------------------------------------
     */
    /*
     *--------------------  操作默认的jedisPoolMap start  --------------------------------------------------
     */

    /**
     * jedis 操作
     */
    public static <R> R op(String mapKey, Function<ShardedJedis, Object> f, Class<R> clazz) {
        return (R) op(mapKey, f);
    }

    /**
     * jedis 操作
     */
    public static Object op(String mapKey, Function<ShardedJedis, Object> f) {
        Object res = null;
        ShardedJedis jedis = null;
        try {
            jedis = getJedis(mapKey);
            res = f.apply(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return res;
    }

    /**
     * 向 redis 存值
     *
     * @param key                 key
     * @param value               value
     * @param time                过期时间的值
     * @param timeUnit:过期时间单位,只接受 SECONDS,MINUTES,HOURS,DAYS , 默认SECONDS
     */
    public static String set(String mapKey, String key, String value, long time, TimeUnit timeUnit) {
        return op(mapKey, (jedis) -> {
            String nxxx = "NX";
            if (jedis.exists(key)) {
                nxxx = "XX";
            }
            //EX = seconds, 秒; PX = milliseconds 毫秒
            String expx = "EX"; //秒
            long t = computationTime(time, timeUnit);
            return jedis.set(key, value, nxxx, expx, t);
        }, String.class);
    }

    public static String set(String mapKey, String key, String value, String nxxx, String expx, long time) {
        return op(mapKey, (jedis) -> {
            return jedis.set(key, value, nxxx, expx, time);
        }, String.class);
    }

    /**
     * 返回 key 对应的值
     */
    public static String get(String mapKey, String key) {
        return op(mapKey, (jedis) -> {
            return jedis.get(key);
        }, String.class);
    }
    /*
     *--------------------  操作默认的jedisPoolMap end  --------------------------------------------------
     */


    /**
     * 计算时间,返回单位为秒的时间值
     */
    public static long computationTime(long time, TimeUnit timeUnit) {
        long t = time;
        if (timeUnit == TimeUnit.MINUTES) {
            t = time * 60;
        } else if (timeUnit == TimeUnit.HOURS) {
            t = time * 60 * 60;
        } else if (timeUnit == TimeUnit.DAYS) {
            t = time * 60 * 60 * 24;
        }
        return t;
    }

    /**
     * 获取  jedis对象
     */
    public static ShardedJedis getJedis(String mapKey) {
        if (StringUtil.isEmpty(mapKey)) {
            vifNull();
            return jedisPool.getResource();
        } else {
            return jedisPoolMap.get(mapKey).getResource();
        }
    }
}
