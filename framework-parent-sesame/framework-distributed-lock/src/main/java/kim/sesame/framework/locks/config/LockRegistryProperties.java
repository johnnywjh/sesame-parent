package kim.sesame.framework.locks.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 锁注册器配置文件
 *
 **/
@ConfigurationProperties(prefix = "framework.locks")
public class LockRegistryProperties {

    /**
     * 失效本地锁Job执行时间
     */
    private long expireJobFixedDelay = 3600000;

    /**
     * 默认 Redis 锁配置
     */
    private Redis redis = new Redis();
    /**
     * 默认Jdbc锁配置
     */
    private Jdbc jdbc = new Jdbc();




    public long getExpireJobFixedDelay() {
        return expireJobFixedDelay;
    }

    public void setExpireJobFixedDelay(long expireJobFixedDelay) {
        this.expireJobFixedDelay = expireJobFixedDelay;
    }

    public Redis getRedis() {
        return redis;
    }

    public void setRedis(Redis redis) {
        this.redis = redis;
    }

    public Jdbc getJdbc() {
        return jdbc;
    }

    public void setJdbc(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    public static class Redis{
        /**
         * 默认可用
         */
        private boolean enable = true;

        private  String registryKey = "locks:default";
        /**
         * 默认 1分钟
         */
        private  long expireAfter = 60000;
        /**
         * 默认 2小时
         */
        private  long defaultExpireUnusedOlderThanTime = 1000 * 60 * 60 * 2;

        public String getRegistryKey() {
            return registryKey;
        }

        public void setRegistryKey(String registryKey) {
            this.registryKey = registryKey;
        }

        public long getExpireAfter() {
            return expireAfter;
        }

        public void setExpireAfter(long expireAfter) {
            this.expireAfter = expireAfter;
        }

        public long getDefaultExpireUnusedOlderThanTime() {
            return defaultExpireUnusedOlderThanTime;
        }

        public void setDefaultExpireUnusedOlderThanTime(long defaultExpireUnusedOlderThanTime) {
            this.defaultExpireUnusedOlderThanTime = defaultExpireUnusedOlderThanTime;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }
    }


    public static class Jdbc{
        /**
         * 是否可用
         * 默认不可用
         */
        private boolean enable = false;
        /**
         * 失效时间 默认10秒
         */
        private int timeToLive = 10000;
        /**
         * 表名
         */
        private String tableName = "T_LOCK";
        /**
         *
         */
        private String region = "DEFAULT";

        public int getTimeToLive() {
            return timeToLive;
        }

        public void setTimeToLive(int timeToLive) {
            this.timeToLive = timeToLive;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }
    }
}
