package kim.sesame.framework.rocketmq.core;

public interface RocketMQConsumerLifecycleListener<T> {
    void prepareStart(final T consumer);
}
