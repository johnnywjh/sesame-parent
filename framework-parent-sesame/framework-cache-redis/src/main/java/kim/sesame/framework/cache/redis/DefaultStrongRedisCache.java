package kim.sesame.framework.cache.redis;


import kim.sesame.framework.cache.CacheManager;
import kim.sesame.framework.cache.IRefreshableCache;
import kim.sesame.framework.cache.exception.KeyIsNotFoundException;
import kim.sesame.framework.cache.exception.ValueIsBlankException;
import kim.sesame.framework.cache.exception.ValueIsNullException;
import kim.sesame.framework.cache.provider.IBatchCacheProvider;
import kim.sesame.framework.cache.redis.storage.RedisCacheStorage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.RedisConnectionFailureException;

import java.util.Date;
import java.util.Map;

public abstract class DefaultStrongRedisCache <K, V>  implements IRefreshableCache<K, V>, InitializingBean, DisposableBean {
    private Log log = LogFactory.getLog(getClass());


    private IBatchCacheProvider<K, V> cacheProvider;
    
    private RedisCacheStorage<K, V> cacheStorage;
    
    /**
     * 自动刷新间隔时间，单位秒，默认15分钟。
     */
    private long interval = 15L * 60 * 1000;
    
    /**
     * 缓存最后一次修改时间,作为刷新缓存的时间戳
     */
    private Date modifyTime;
    
    /**
     * 自动刷新缓存线程
     */
    private ReloadThread thread;
    
    private String prefix = "framework.redis.strong.initialization.";
    
    public void setInterval(int seconds) {
        this.interval = (long) seconds * 1000;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterPropertiesSet() throws Exception {
        CacheManager.getInstance().registerCacheProvider(this);
        cacheStorage.initializationStrongCache(getUUID(), cacheProvider.get());
        modifyTime = cacheProvider.getLastModifyTime();
        //启动刷新线程
        thread = new ReloadThread("STRONG_REDIS_CACHE_" + this.getUUID());
        thread.setDaemon(true);
        thread.start();
    }
    
    @Override
    public V get(K key) {
        V value = null;
        try {
            value = cacheStorage.hget(getUUID(),key);
        } catch(ValueIsBlankException e) {
            //key存在，value为空串
            return null;
        } catch(ValueIsNullException ex) {
            //key存在，value为null
            return null;
        } catch(KeyIsNotFoundException ex1) {
            //key不存在
            return null;
        } catch(RedisConnectionFailureException exx) {
            //redis 连接出现异常
            log.error("redis 连接异常!");
            return null;
        }
        return value;
    }

    @Override
    public Map<K, V> get() {
        try {
            return cacheStorage.hget(getUUID());
        } catch(RedisConnectionFailureException e) {
            //redis 连接出现异常
            log.error("redis 连接异常!");
            return cacheProvider.get();
        }
        
    }
    
    public void setCacheProvider(IBatchCacheProvider<K, V> cacheProvider) {
        this.cacheProvider = cacheProvider;
    }
    
    public void setCacheStorage(RedisCacheStorage<K, V> cacheStorage) {
        this.cacheStorage = cacheStorage;
    }

    @Override
    public void destroy() throws Exception {
    }
    /**
     * 
     * 自动刷新缓存
     * @since
     * @version
     */
    private class ReloadThread extends Thread {
        private volatile int state;

        ReloadThread(String threadName) {
            super(threadName);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    state = 2;
                    //刷新间隔时间
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                   // Log.error(e.getMessage());
                	e.printStackTrace();
                    break;
                }
                try {
                    state = 1;
                    //如果最后更新时间早于当前时间
                    //更新所有数据
                    refresh();
                } catch (Exception ex) {
                    //Log.error(ex.getMessage());
                	ex.printStackTrace();
                    break;
                }
            }
        }   

    }

    /**
     * 
     * <p>刷新策略</p> 
     * @return 
     */
    @Override
    public boolean refresh() {
        Date lastTime = cacheProvider.getLastModifyTime();
        if (modifyTime != null && lastTime != null && lastTime.after(modifyTime)) {
            Map<K, V> map = cacheProvider.get();
            for (Map.Entry<K, V> entry : map.entrySet()) {
                cacheStorage.hset(getUUID(), entry.getKey(), entry.getValue());
            }
            modifyTime = lastTime;
            return true;
        }
        return false;
    }

    @Override
    public boolean refresh(K... keys) {
        throw new RuntimeException("Strong Cache Cannot Refresh Part!");
    }
    /**
     * 
     * <p>重新初始化缓存数据</p> 
     */
    public void invalid() {
        cacheStorage.hremove(getUUID());
        cacheStorage.hremove(prefix + getUUID());
        //因为是批量加载所以全部失效考虑重新加载的问题
        cacheStorage.initializationStrongCache(getUUID(), cacheProvider.get());
        modifyTime = cacheProvider.getLastModifyTime();
    }
    
    public void invalid(K key) {
        throw new RuntimeException("Strong Cache Cannot Invalid Part!");
        //cacheStorage.hremove(getUUID(),key);
    }
    
    public void invalidMulti(K ... keys) {
        throw new RuntimeException("Strong Cache Cannot Invalid Part!");
    }
    
//    public Boolean exists(K key) {
//        return cacheStorage.hexists(getUUID(),key);
//    }
}
