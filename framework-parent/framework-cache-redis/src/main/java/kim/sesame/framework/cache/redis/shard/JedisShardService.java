package kim.sesame.framework.cache.redis.shard;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class JedisShardService {
    static ShardedJedisPool jedisPool = null;

    private static void vifNull() {
        if (jedisPool == null) {
            throw new NullPointerException("jedis 连接池为空,配置:sesame.framework.shard.hosts");
        }
    }

    /**
     * jedis 操作
     */
    public static <R> R op(Function<ShardedJedis, Object> f, Class<R> clazz) {
        return (R) op(f);
    }

    /**
     * jedis 操作
     */
    public static Object op(Function<ShardedJedis, Object> f) {
        vifNull();
        Object res = null;
        ShardedJedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            res = f.apply(jedis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return res;
    }

    /**
     * 像 redis 存值
     *
     * @param key                 key
     * @param value               value
     * @param time                过期时间的值
     * @param timeUnit:过期时间单位,只接受 SECONDS,MINUTES,HOURS,DAYS , 默认SECONDS
     */
    public static String set(String key, String value, long time, TimeUnit timeUnit) {
        return op((jedis) -> {
            String nxxx = "NX";
            if (jedis.exists(key)) {
                nxxx = "XX";
            }
            //EX = seconds, 秒; PX = milliseconds 毫秒
            String expx = "EX"; //秒
            long t = time;
            if (timeUnit == TimeUnit.MINUTES) {
                t = time * 60;
            } else if (timeUnit == TimeUnit.HOURS) {
                t = time * 60 * 60;
            } else if (timeUnit == TimeUnit.DAYS) {
                t = time * 60 * 60 * 24;
            }
            return jedis.set(key, value, nxxx, expx, t);
        }, String.class);
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
        return op((jedis) -> {
            return jedis.set(key, value, nxxx, expx, time);
        }, String.class);
    }

    /**
     * 返回 key 对应的值
     */
    public static String get(String key) {
        return op((jedis) -> {
            return jedis.get(key);
        }, String.class);
    }


}
