package kim.sesame.framework.cache.redis.config;


import kim.sesame.framework.cache.redis.server.DynamicStringRedisTemplate;
import kim.sesame.framework.web.context.SpringContextUtil;
import lombok.Data;
import org.apache.commons.pool2.impl.BaseObjectPoolConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * https://www.cnblogs.com/yjmyzz/p/how-to-inject-multi-redis-instance-in-spring-boot.html
 */
@Data
@Component
@ConfigurationProperties(prefix = "sesame.framework.redis")
public class DynamicRedisProperties implements InitializingBean {

    private String groupName = "sesame-redis";

    private Map<String, RedisNode> nodes;

    @Data
    public static class RedisNode {

        private int dbIndex = 0;
        private String hostName = "localhost";
        private int port = Protocol.DEFAULT_PORT;
        private String password;

        private int maxTotal = GenericObjectPoolConfig.DEFAULT_MAX_TOTAL;
        private int minIdle = GenericObjectPoolConfig.DEFAULT_MIN_IDLE;
        private int maxIdle = GenericObjectPoolConfig.DEFAULT_MAX_IDLE;
        private long maxWaitMillis = BaseObjectPoolConfig.DEFAULT_MAX_WAIT_MILLIS;
        private boolean testOnBorrow = BaseObjectPoolConfig.DEFAULT_TEST_ON_BORROW;
        private boolean testOnReturn = BaseObjectPoolConfig.DEFAULT_TEST_ON_RETURN;
        private int timeout = Protocol.DEFAULT_TIMEOUT;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<String> keys = nodes.keySet();
        if (keys.size() == 0) {
            return;
        }
        for (String key : keys) {
            RedisNode node = nodes.get(key);
            StringRedisTemplate redisTemplate = redisTemplate(buildConnectionFactory(node));
            Map<String,Object> map = new HashMap<>();
            map.put("stringRedisTemplate",redisTemplate);

            redisTemplate.opsForValue().set("aaaaaaaa","123456",2,TimeUnit.MINUTES);
           // SpringContextUtil.registerBean(key,map,DynamicStringRedisTemplate.class);
        }


    }

    public JedisPoolConfig jedisPoolConfig(RedisNode node) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(node.getMaxTotal());
        poolConfig.setMinIdle(node.getMinIdle());
        poolConfig.setMaxIdle(node.getMaxIdle());
        poolConfig.setMaxWaitMillis(node.getMaxWaitMillis());
        poolConfig.setTestOnBorrow(node.isTestOnBorrow());
        poolConfig.setTestOnReturn(node.isTestOnReturn());
        return poolConfig;
    }

    private JedisConnectionFactory buildConnectionFactory(RedisNode node) {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(jedisPoolConfig(node));
        connectionFactory.setHostName(node.getHostName());
        connectionFactory.setPassword(node.getPassword());
        connectionFactory.setPort(node.getPort());
        connectionFactory.setDatabase(node.getDbIndex());
        connectionFactory.setUsePool(true);
        connectionFactory.setTimeout(node.getTimeout());
        connectionFactory.afterPropertiesSet();
        return connectionFactory;
    }

    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    private StringRedisTemplate redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(jedisConnectionFactory);
        template.setValueSerializer(stringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }


}
