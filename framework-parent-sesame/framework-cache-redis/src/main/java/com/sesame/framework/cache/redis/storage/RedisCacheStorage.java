package com.sesame.framework.cache.redis.storage;

import com.sesame.framework.cache.exception.KeyIsNotFoundException;
import com.sesame.framework.cache.exception.ValueIsNullException;
import com.sesame.framework.cache.redis.exception.RedisCacheStorageException;
import com.sesame.framework.cache.storage.IRemoteCacheStore;
import com.sesame.framework.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @param <K>
 * @param <V>
 * @ClassName: RedisCacheStorage
 * @Description: 通过redis存储缓存数据
 * 1.支持序列化json
 * @date 2015年4月22日 下午1:27:34
 */
public class RedisCacheStorage<K, V> implements IRemoteCacheStore<K, V>,
        InitializingBean {

    private RedisTemplate redisTemplate;

    /**
     * 日志
     */
    Log log = LogFactory.getLog(getClass());

    /**
     * 默认数据过期时间
     */
    private int expire = 60 * 10;

    /**
     * 初始化Strong cache任务
     */
    private StrongCacheTask strongTask;

    /**
     * 稍后重试初始化Strong cache 延迟时间，默认1分钟
     */
    private int strongDelayTime = 60 * 1000;

    /**
     * 初始化加锁key
     */
    private final String lockKeyPrefix = "framework.redis.lock.";
    /**
     * 初始化加锁Value
     */
    private final String lockValue = "1";
    /**
     * 是否初始化成功
     */
    private final String initSuccessPrefix = "framework.redis.strong.initialization.";


    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * <p>存入数据，默认时效：3600 * 24</p>
     *
     * @param key
     * @param value
     * @return boolean 是否执行成功
     */
    public boolean set(K key, V value) {
        return set(key, value, expire);
    }


    /**
     * <p>存入有时效的数据</p>
     *
     * @param key
     * @param value
     * @param exp
     * @return boolean 是否执行成功
     */
    public boolean set(K key, V value, int exp) {
        if (key == null) {
            throw new RedisCacheStorageException("key does not allow for null!");
        }
        redisTemplate.opsForValue().set(key, value, exp, TimeUnit.SECONDS);
        return true;
    }

    /**
     * <p>获取key对应的数据</p>
     *
     * @param key
     * @return
     */
    public V get(K key) {
        if (key == null) {
            throw new RedisCacheStorageException("key does not allow for null!");
        }
        //key是否存在
        boolean exist = redisTemplate.hasKey(key);
        if (!exist) {
            throw new KeyIsNotFoundException("key is not found!");
        }
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj == null) {
            throw new ValueIsNullException("key exists, value is null!");
        }
        return (V) obj;
    }

    /**
     * <p>删除指定的缓存信息</p>
     *
     * @param key
     */
    public void remove(K key) {
        redisTemplate.delete(key);
    }

    /**
     * <p>删除多个key的缓存信息</p>
     *
     * @param keys
     */
    public void removeMulti(K... keys) {
        redisTemplate.delete(keys);
    }


    /**
     * 获取key对应的数据
     *
     * @return
     * @see
     */
    @SuppressWarnings("unchecked")
    public V hget(String cacheId, K key) {
        if (key == null) {
            throw new RedisCacheStorageException("key does not allow for null!");
        }
        //key是否存在
        boolean exist = redisTemplate.opsForHash().hasKey(cacheId, key);
        if (!exist) {
            throw new KeyIsNotFoundException("key is not found!");
        }
        Object obj = redisTemplate.opsForHash().get(cacheId, key);
        if (obj == null) {
            throw new ValueIsNullException("key exists, value is null!");
        }
        return (V) obj;
    }

    /**
     * 存入数据
     *
     * @param cacheId
     * @param key
     * @param value
     * @return 执行成功与否
     * @see
     */
    public boolean hset(String cacheId, K key, V value) {
        if (key == null) {
            throw new RedisCacheStorageException("key does not allow for null!");
        }
        redisTemplate.opsForHash().put(cacheId, key, value);
        return true;
    }

    public Map<K, V> hget(String cacheId) {

        if(StringUtil.isBlank(cacheId)){
            throw new RedisCacheStorageException("cacheId does not allow for null!");
        }
        Map<Object, Object> map = redisTemplate.opsForHash().entries(cacheId);
        Map<K, V> result = null;
        //由string转成对象
        if (map != null) {
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                if (result == null) {
                    result = new HashMap<K, V>();
                }
                result.put((K) entry.getKey(), (V) entry.getValue());
            }
            return result;
        }
        return null;
    }

    public void hremoveMulti(final String cacheId, final K... keys) {
        if (StringUtils.isBlank(cacheId)) {
            throw new RedisCacheStorageException("cacheId does not allow for null!");
        }
        if (keys == null) {
            throw new RedisCacheStorageException("keys does not allow for null!");
        }
        redisTemplate.opsForHash().delete(cacheId, keys);
    }

    /**
     * 删除
     *
     * @param cacheId
     * @param key
     * @see
     */
    public void hremove(final String cacheId, final K key) {

        if (StringUtils.isBlank(cacheId)) {
            throw new RedisCacheStorageException("cacheId does not allow for null!");
        }
        if (key == null) {
            throw new RedisCacheStorageException("key does not allow for null!");
        }
        redisTemplate.opsForHash().delete(cacheId, key);
    }

    public void hremove(final String cacheId) {
        if(StringUtils.isBlank(cacheId)) {
            throw new RedisCacheStorageException("cacheId does not allow for null!");
        }
        redisTemplate.delete(cacheId);
    }

    public void initializationStrongCache(final String cacheId, Map<K, V> map) {
        try{
            Boolean isSuccess = (Boolean) redisTemplate.opsForValue().get(initSuccessPrefix + cacheId);
            if (isSuccess == null || !isSuccess) {//如果对应的cacheId 没有初始化过或者初始化失败
                Boolean lock = setNx(lockKeyPrefix + cacheId, lockValue);
                if (lock) {
                    initializationStrongCacheData(cacheId, map);
                } else {
                    // 稍后重试
                    retryStorage(cacheId, map);
                    return;
                }
            }
        }catch (RedisConnectionFailureException e){
            log.error("redis连接出现异常");
        }

    }

    private void initializationStrongCacheData(final String cacheId, Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) {
                log.error("storage cache initialization error: key and value does not allow for null!");
                setNx(initSuccessPrefix + cacheId, "false");
                redisTemplate.delete(lockKeyPrefix + cacheId);
                return;
            }
            redisTemplate.opsForHash().put(cacheId, entry.getKey(), entry.getValue());
        }
        setNx(initSuccessPrefix + cacheId, "true");
        redisTemplate.delete(lockKeyPrefix + cacheId);
    }

    private Boolean setNx(final String key, final String value) {
        return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                StringRedisSerializer serializer = new StringRedisSerializer();
                Boolean success = connection.setNX(serializer.serialize(key), serializer.serialize(value));
                return success;
            }
        });
    }

    /**
     * 开启初始化StrongCache线程任务
     *
     * @see
     */
    private void retryStorage(String cacheId, Map<K, V> map) {
        if (strongTask == null) {
            strongTask = new StrongCacheTask("重试初始化Strong Cache任务", cacheId, map);
            strongTask.setDaemon(true);
            strongTask.start();
        } else if (strongTask.getState().name().equals(Thread.State.NEW.name())) {
            strongTask.start();
        } else if (strongTask.getState().name().equals(Thread.State.TERMINATED.name())) {
            strongTask = new StrongCacheTask("重试初始化Strong Cache任务", cacheId, map);
            strongTask.setDaemon(true);
            strongTask.start();
        }
    }

    /**
     * 初始化Strong Cache任务
     */
    class StrongCacheTask extends Thread {
        int count = 1;
        String cacheId;
        Map<K, V> map;

        public StrongCacheTask(String name, String cacheId, Map<K, V> map) {
            super(name);
            count = 1;
            this.cacheId = cacheId;
            this.map = map;
        }

        @SuppressWarnings("static-access")
        @Override
        public void run() {
            log.debug("初始化Strong Cache任务开始，延迟" + strongDelayTime + "后开始执行!");

            while (true) {
                if (map == null || map.isEmpty()) {
                    return;
                }
                try {
                    this.sleep(strongDelayTime);
                } catch (InterruptedException e1) {
                    log.error(e1.getMessage(), e1);
                }
                log.debug("第" + count + "次尝试初始化Strong Cache!");
                count++;
                Boolean isSuccess = (Boolean) redisTemplate.opsForValue().get(initSuccessPrefix + cacheId);
                if (isSuccess == null || !isSuccess) {//如果对应的cacheId 没有初始化过或者初始化失败
                    Boolean lock = setNx(lockKeyPrefix + cacheId, lockValue);
                    if (lock) {
                        initializationStrongCacheData(cacheId, map);
                    } else {
                        continue;
                    }
                }
            }
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public void setExpire(int expire) {
        this.expire = expire;
    }
}
