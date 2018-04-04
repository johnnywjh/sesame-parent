package kim.sesame.framework.lock.service.impl;

import kim.sesame.framework.lock.service.DistributedLocker;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedissonLock implements DistributedLocker {

    private RedissonClient redissonClient;
    /***
     *  Author:  wenlin
     *  Date:  2018/4/4
     *  @param lockKey 锁名
     *  Description:加锁
     **/
    @Override
    public void lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
    }
    /***
     *  Author:  wenlin
     *  Date:  2018/4/4
     *  @param lockKey 锁名
     *  Description:解锁
     **/
    @Override
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }
    /***
     *  Author:  wenlin
     *  Date:  2018/4/4
     *  @param lockKey 锁名
     *  @param leaseTime 过期时间(单位：秒)
     *  Description:带有过期时间的锁
     **/
    @Override
    public void lock(String lockKey, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, TimeUnit.SECONDS);
    }
    /***
     *  Author:  wenlin
     *  Date:  2018/4/4
     *  @param lockKey 锁名
     *  @param unit 时间单位
     *  @param timeout 过期时间
     *  Description:指定锁名称，并且带过期时间
     **/
    @Override
    public void lock(String lockKey, TimeUnit unit, int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
    }
    /***
     *  Author:  wenlin
     *  Date:  2018/4/4
     *  @param lockKey 锁名
     *  @param tryTime 尝试时间
     *  @param timeout 锁过期时间
     *  Description:尝试获取锁
     **/
    @Override
    public Boolean tryLock(String lockKey, Long tryTime, Long timeout) throws InterruptedException {
        RLock lock = redissonClient.getLock(lockKey);
        return lock.tryLock(tryTime,timeout,TimeUnit.SECONDS);
    }
    /***
     *  Author:  wenlin
     *  Date:  2018/4/4
     *  @param lockKey 锁名
     *  @param tryTime 尝试时间
     *  @param timeout 锁过期时间
     *  Description:尝试获取公平锁，公平锁可以保证多个客户端请求按时间公平加锁
     **/
    @Override
    public Boolean tryFairLock(String lockKey, Long tryTime, Long timeout) throws InterruptedException {
        RLock lock = redissonClient.getFairLock(lockKey);
        return lock.tryLock(tryTime,timeout,TimeUnit.SECONDS);
    }

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }
}