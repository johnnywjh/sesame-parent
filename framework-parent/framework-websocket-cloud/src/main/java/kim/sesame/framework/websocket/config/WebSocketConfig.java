package kim.sesame.framework.websocket.config;

public class WebSocketConfig {
    /**
     * 尝试次数, 默认5次
     */
    private static int totalCount = 5;

    /**
     * 每一次等待时间,单位秒, 默认1分钟
     */
    private static int failRetryTime = 60;

    public static int getTotalCount() {
        return totalCount;
    }

    static void setTotalCount(int totalCount) {
        WebSocketConfig.totalCount = totalCount;
    }

    public static int getFailRetryTime() {
        return failRetryTime;
    }

    static void setFailRetryTime(int failRetryTime) {
        WebSocketConfig.failRetryTime = failRetryTime;
    }
}
