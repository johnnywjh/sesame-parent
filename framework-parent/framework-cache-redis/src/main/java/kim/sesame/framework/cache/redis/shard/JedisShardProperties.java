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

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "sesame.framework.shard")
public class JedisShardProperties implements InitializingBean {

    private List<String> hosts;
    private List<String> pwds;

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
            shardInfo = new JedisShardInfo(h.getHost(), h.getPort(), 500);
            if (StringUtil.isNotEmpty(h.getPwd())) {
                shardInfo.setPassword(h.getPwd());
            }
            infoList.add(shardInfo);
        }
        JedisShardService.jedisPool = new ShardedJedisPool(poolConfig, infoList);
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
