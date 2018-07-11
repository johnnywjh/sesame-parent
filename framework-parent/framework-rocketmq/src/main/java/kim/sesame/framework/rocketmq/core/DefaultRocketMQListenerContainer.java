package kim.sesame.framework.rocketmq.core;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;


@Slf4j
public class DefaultRocketMQListenerContainer implements InitializingBean, RocketMQListenerContainer {

    @Setter
    @Getter
    private long suspendCurrentQueueTimeMillis = 1000;

    /**
     * Message consume retry strategy<br> -1,no retry,put into DLQ directly<br> 0,broker control retry frequency<br>
     * >0,client control retry frequency
     */
    @Setter
    @Getter
    private int delayLevelWhenNextConsume;

    @Setter
    @Getter
    private String consumerGroup;

    @Setter
    @Getter
    private String nameServer;

    @Setter
    @Getter
    private String topic;

    @Setter
    @Getter
    private ConsumeMode consumeMode = ConsumeMode.CONCURRENTLY;

    @Setter
    @Getter
    private SelectorType selectorType = SelectorType.TAG;

    @Setter
    @Getter
    private String selectorExpress = "*";

    @Setter
    @Getter
    private MessageModel messageModel = MessageModel.CLUSTERING;

    /**
     * 最大线程数
     */
    @Setter
    @Getter
    private int consumeThreadMax = 64;

    /**
     * 最小线程数
     */
    @Setter
    @Getter
    private int consumeThreadMin = 20;

    /**
     * 批量消费数量
     *
     * @return
     * @see MessageListenerConcurrently#consumeMessage
     */
    @Setter
    @Getter
    private int consumeMessageBatchMaxSize = 1;

    /**
     * 批量拉取消息数量
     *
     * @return
     */
    @Setter
    @Getter
    private int pullBatchSize = 32;

    @Setter
    @Getter
    private int pollNameServerInterval = 1000 * 30;

    @Setter
    @Getter
    private int heartbeatBrokerInterval = 1000 * 30;

    @Getter
    @Setter
    private String charset = "UTF-8";

    @Getter
    @Setter
    private RocketMQSerializer serializer = new FastJsonRocketMQSerializer();

    @Setter
    @Getter
    private boolean started;

    @Setter
    private RocketMQListener rocketMQListener;

    private DefaultMQPushConsumer consumer;

    private Class messageType;

    public void setupMessageListener(RocketMQListener rocketMQListener) {
        this.rocketMQListener = rocketMQListener;
    }

    @Override
    public void destroy() throws Exception {
        this.setStarted(false);
        if (Objects.nonNull(consumer)) {
            consumer.shutdown();
        }
        log.info("container destroyed, {}", this.toString());
    }

    public synchronized void start() throws MQClientException {

        if (this.isStarted()) {
            throw new IllegalStateException("container already started. " + this.toString());
        }

        initRocketMQPushConsumer();

        // parse message type
        this.messageType = getMessageType();
        log.debug("msgType: {}", messageType.getName());

        consumer.start();
        this.setStarted(true);

        log.info("started container: {}", this.toString());
    }

    public class DefaultMessageListenerConcurrently implements MessageListenerConcurrently {

        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
            for (MessageExt messageExt : msgs) {
                log.debug("received msg: {}", messageExt);
                try {
                    long now = System.currentTimeMillis();
                    rocketMQListener.onMessage(doConvertMessage(messageExt), messageExt);
                    long costTime = System.currentTimeMillis() - now;
                    log.info("consume {} cost: {} ms", messageExt.getMsgId(), costTime);
                } catch (Exception e) {
                    log.warn("consume message failed. messageExt:{}", messageExt, e);
                    context.setDelayLevelWhenNextConsume(delayLevelWhenNextConsume);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
    }

    public class DefaultMessageListenerOrderly implements MessageListenerOrderly {

        public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
            for (MessageExt messageExt : msgs) {
                log.debug("received msg: {}", messageExt);
                try {
                    long now = System.currentTimeMillis();
                    rocketMQListener.onMessage(doConvertMessage(messageExt), messageExt);
                    long costTime = System.currentTimeMillis() - now;
                    log.debug("consume {} cost: {} ms", messageExt.getMsgId(), costTime);
                } catch (Exception e) {
                    log.warn("consume message failed. messageExt:{}", messageExt, e);
                    context.setSuspendCurrentQueueTimeMillis(suspendCurrentQueueTimeMillis);
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }
            }

            return ConsumeOrderlyStatus.SUCCESS;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }

    @SuppressWarnings("unchecked")
    private Object doConvertMessage(MessageExt messageExt) {
        if (Objects.equals(messageType, MessageExt.class)) {
            return messageExt;
        } else {
            if (Objects.equals(messageType, String.class)) {
                return new String(messageExt.getBody(), Charset.forName(charset));
            } else {
                return serializer.deserialize(messageExt.getBody(), messageType);
            }
        }
    }

    private Class getMessageType() {
        Type[] interfaces = rocketMQListener.getClass().getGenericInterfaces();
        if (Objects.nonNull(interfaces)) {
            for (Type type : interfaces) {
                if (type instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    if (Objects.equals(parameterizedType.getRawType(), RocketMQListener.class)) {
                        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                        if (Objects.nonNull(actualTypeArguments) && actualTypeArguments.length > 0) {
                            return (Class) actualTypeArguments[0];
                        } else {
                            return Object.class;
                        }
                    }
                }
            }

            return Object.class;
        } else {
            return Object.class;
        }
    }

    private void initRocketMQPushConsumer() throws MQClientException {

        Assert.notNull(rocketMQListener, "Property 'rocketMQListener' is required");
        Assert.notNull(consumerGroup, "Property 'consumerGroup' is required");
        Assert.notNull(nameServer, "Property 'nameServer' is required");
        Assert.notNull(topic, "Property 'topic' is required");

        consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(nameServer);
        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setMessageModel(messageModel);

        switch (selectorType) {
            case TAG:
                consumer.subscribe(topic, selectorExpress);
                break;
            case SQL92:
                consumer.subscribe(topic, MessageSelector.bySql(selectorExpress));
                break;
            default:
                throw new IllegalArgumentException("Property 'selectorType' was wrong.");
        }

        switch (consumeMode) {
            case ORDERLY:
                consumer.setMessageListener(new DefaultMessageListenerOrderly());
                break;
            case CONCURRENTLY:
                consumer.setMessageListener(new DefaultMessageListenerConcurrently());
                break;
            default:
                throw new IllegalArgumentException("Property 'consumeMode' was wrong.");
        }

        // provide an entryway to custom setting RocketMQ consumer
        if (rocketMQListener instanceof RocketMQPushConsumerLifecycleListener) {
            ((RocketMQPushConsumerLifecycleListener) rocketMQListener).prepareStart(consumer);
        }

    }

}
