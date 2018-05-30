package kim.sesame.framework.cache.redis.shard;

import kim.sesame.framework.utils.StringUtil;
import lombok.Data;
import org.apache.commons.pool2.impl.BaseObjectPoolConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "sesame.framework.shard")
public class JedisShardProperties implements InitializingBean {

    /**
     * redis 连接信息,格式 : ip:port,ip:port
     */
    private List<String> hosts;
    /**
     * redis 连接对应的密码,格式 : pwd,pwd,pwd
     * 注意: 如果只写一个,表示所有的redis密码都是这个
     */
    private List<String> pwds;
    /**
     * 连接超时时间,一般不设置,默认0,jedis 默认2000
     */
    private int connectionTimeout = 0;
    /**
     * key 的算法,默认Hashing.MURMUR_HASH
     * 两个值 MURMUR_HASH , MD5
     */
    private String keyAlgo = null;

    private Pool pool;

    @Data
    public static class Pool {
        private int maxTotal = GenericObjectPoolConfig.DEFAULT_MAX_TOTAL;
        private int maxIdle = GenericObjectPoolConfig.DEFAULT_MAX_IDLE;

        private long maxWaitMillis = BaseObjectPoolConfig.DEFAULT_MAX_WAIT_MILLIS;
        private boolean testOnBorrow = BaseObjectPoolConfig.DEFAULT_TEST_ON_BORROW;
        private boolean testOnReturn = BaseObjectPoolConfig.DEFAULT_TEST_ON_RETURN;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (hosts == null || hosts.size() == 0) {
            return;
        }
        if (pool == null) {
            pool = new Pool();
        }
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(pool.getMaxTotal());
        poolConfig.setMaxIdle(pool.getMaxIdle());
        poolConfig.setMaxWaitMillis(pool.getMaxWaitMillis());
        poolConfig.setTestOnBorrow(pool.isTestOnBorrow());
        poolConfig.setTestOnReturn(pool.isTestOnReturn());

        List<JedisShardInfo> infoList = new ArrayList<>();
        JedisShardInfo shardInfo = null;
        String host = null;
        int port = 0;
        String pwd = null;
        for (int i = 0; i < hosts.size(); i++) {
            if (StringUtil.isEmpty(hosts.get(i))) {
                continue;
            }
            Host h = new Host(this.hosts, this.pwds, i);
            shardInfo = new JedisShardInfo(h.getHost(), h.getPort());
            if (this.connectionTimeout > 0) {
                shardInfo.setConnectionTimeout(connectionTimeout);
            }
            if (StringUtil.isNotEmpty(h.getPwd())) {
                shardInfo.setPassword(h.getPwd());
            }
            infoList.add(shardInfo);
        }
        // 判断key的算法
        if (StringUtil.isNotEmpty(keyAlgo) && keyAlgo.toUpperCase().equals("MD5")) {
            JedisShardService.jedisPool = new ShardedJedisPool(poolConfig, infoList, Hashing.MD5);
        } else {
            JedisShardService.jedisPool = new ShardedJedisPool(poolConfig, infoList);
        }


//        ShardedJedis jedis = jedisPool.getResource();
    }

    @Data
    class Host {
        private String host;
        private int port;
        private String pwd;

        public Host() {
        }

        public Host(List<String> hosts, List<String> pwds, int i) {
            String host_pwd = hosts.get(i);
            String[] arr = host_pwd.split(":");
            if (arr.length == 1) {
                this.host = host_pwd;
                this.port = 6379;
            } else {
                this.host = arr[0];
                this.port = Integer.parseInt(arr[1]);
            }
            if (pwds != null && pwds.size() > 0) {
                if (pwds.size() == 1) {
                    this.pwd = pwds.get(0);
                } else {
                    this.pwd = pwds.get(i);
                }
            }
        }

    }
}
