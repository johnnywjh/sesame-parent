package kim.sesame.framework.cache.redis.shard;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.concurrent.TimeUnit;

public class JedisShardService {
    static ShardedJedisPool jedisPool = null;

    private static void vifNull() {
        if (jedisPool == null) {
            throw new NullPointerException("jedis 连接池为空,配置:sesame.framework.shard.hosts");
        }
    }

    /**
     * 像 redis 存值
     *
     * @param key                 key
     * @param value               value
     * @param timeUnit:过期时间单位,只接受 SECONDS,MINUTES,HOURS,DAYS , 默认SECONDS
     * @param time                过期时间的值
     */
    public static String set(String key, String value, TimeUnit timeUnit, long time) {
        vifNull();
        String res = null;
        ShardedJedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String nxxx = "NX";
            if (jedis.exists(key)) {
                nxxx = "XX";
            }
            //EX = seconds, 秒; PX = milliseconds 毫秒
            String expx = "EX"; //秒
            if (timeUnit == TimeUnit.MINUTES) {
                time = time * 60;
            } else if (timeUnit == TimeUnit.HOURS) {
                time = time * 60 * 60;
            } else if (timeUnit == TimeUnit.DAYS) {
                time = time * 60 * 60 * 24;
            }

            res = jedis.set(key, value, nxxx, expx, time);
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
        vifNull();
        String res = null;
        ShardedJedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            res = jedis.set(key, value, nxxx, expx, time);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return res;
    }

    public static String get(String key) {
        vifNull();
        ShardedJedis jedis = null;
        String res = null;
        try {
            jedis = jedisPool.getResource();
            res = jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return res;
    }
}
