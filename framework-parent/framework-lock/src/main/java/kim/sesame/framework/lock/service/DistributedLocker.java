package kim.sesame.framework.lock.service;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁的接口
 *
 * @author johnny
 * date :  2017/12/11 16:46
 */
public interface DistributedLocker {
    void lock(String lockKey);

    void unlock(String lockKey);

    void lock(String lockKey, int timeout);

    void lock(String lockKey, TimeUnit unit, int timeout);

    Boolean tryLock(String lockKey,Long tryTime,Long timeout) throws InterruptedException;

    Boolean tryFairLock(String lockKey,Long tryTime,Long timeout) throws InterruptedException;
}
