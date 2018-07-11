package kim.sesame.framework.rocketmq.core;

import org.apache.rocketmq.common.message.MessageExt;

/**
 *
 * @param <T>
 */
public interface RocketMQListener<T> {
    void onMessage(T message, MessageExt messageExt);
}
