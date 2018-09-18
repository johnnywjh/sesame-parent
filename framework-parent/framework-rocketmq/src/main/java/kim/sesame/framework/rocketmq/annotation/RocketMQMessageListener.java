package kim.sesame.framework.rocketmq.annotation;


import kim.sesame.framework.rocketmq.core.ConsumeMode;
import kim.sesame.framework.rocketmq.core.SelectorType;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;


/**
 * RocketMq 消息监听注解
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RocketMQMessageListener {
    /**
     * 默认最小线程
     */
    String DEFAULT_CONSUME_THREAD_MIN = "20";
    /**
     * 默认最大线程
     */
    String DEFAULT_CONSUME_THREAD_MAX = "64";
    /**
     * 默认批量拉取数量
     */
    int DEFAULT_PULL_BATCH_SIZE = 32;

    int DEFAULT_HEART_BROKER_INTERVAL = 30 * 1000;
    /**
     * RocketMQ nameServer
     * @return
     */
    String nameServer() default "";

    /**
     * 消费组
     * @return
     */
    String consumerGroup();

    /**
     * Topic
     * @return
     */
    String topic();

    /**
     * 消息表达式类型
     * 默认按照TAG
     * @return
     */
    SelectorType selectorType() default SelectorType.TAG;

    /**
     *
     * @return
     */
    String selectorExpress() default "*";

    /**
     * 默认同时消费
     * @return
     */
    ConsumeMode consumeMode() default ConsumeMode.CONCURRENTLY;

    /**
     * 默认集群消费
     * @return
     */
    MessageModel messageModel() default MessageModel.CLUSTERING;

    /**
     * 消费者最小线程
     * @return
     */
    String consumeThreadMin() default DEFAULT_CONSUME_THREAD_MIN;

    /**
     * 消费者最大线程
     * @return
     */
    String consumeThreadMax() default DEFAULT_CONSUME_THREAD_MAX;

    /**
     * 批量消费数量
     * @see org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently#consumeMessage
     * @return
     */
    int consumeMessageBatchMaxSize() default 1;

    /**
     * 批量拉取消息数量
     * @return
     */
    int pullBatchSize() default DEFAULT_PULL_BATCH_SIZE;

    int pollNameServerInterval() default DEFAULT_HEART_BROKER_INTERVAL;

    int heartbeatBrokerInterval() default DEFAULT_HEART_BROKER_INTERVAL;

}
