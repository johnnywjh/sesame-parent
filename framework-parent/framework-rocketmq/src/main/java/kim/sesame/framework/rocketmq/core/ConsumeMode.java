package kim.sesame.framework.rocketmq.core;

/**
 * 消费者模式
 */
public enum ConsumeMode {
    /**
     *
     */
    CONCURRENTLY,

    /**
     * 顺序消费
     */
    ORDERLY
}
