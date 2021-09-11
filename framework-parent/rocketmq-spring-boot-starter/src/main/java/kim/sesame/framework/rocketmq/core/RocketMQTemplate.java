package kim.sesame.framework.rocketmq.core;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * RockMQTemplate
 */
@Slf4j
public class RocketMQTemplate implements InitializingBean, DisposableBean {

    @Getter
    @Setter
    private DefaultMQProducer producer;

    @Getter
    @Setter
    private RocketMQSerializer serializer = new FastJsonRocketMQSerializer();

    @Getter
    @Setter
    private String charset = "UTF-8";

    /**
     * 同步发送消息
     *
     * @param message
     * @return
     */
    public SendResult syncSend(Message message) {
        return this.syncSend(message, producer.getSendMsgTimeout());
    }

    /**
     * 同步发送消息
     *
     * @param message
     * @param timeout
     * @return
     */
    public SendResult syncSend(Message message, long timeout) {
        try {
            SendResult sendResult = producer.send(message, timeout);
            return sendResult;
        } catch (Exception e) {
            log.error("send message failed");
            throw new MessagingException(e.getMessage(), e);
        }
    }

    /**
     * 同步发送消息
     *
     * @param topic
     * @param tag
     * @param msgObj
     * @param properties
     * @return
     */
    public SendResult syncSend(String topic, String tag, Object msgObj, Map<String, Object> properties) {
        return this.syncSend(topic, tag, msgObj, properties, this.producer.getSendMsgTimeout());
    }

    /**
     * 同步发送消息
     *
     * @param topic
     * @param tag
     * @param msgObj
     * @param properties
     * @param timeout
     * @return
     */
    public SendResult syncSend(String topic, String tag, Object msgObj, Map<String, Object> properties, long timeout) {
        Message message = createMessage(topic, tag, msgObj, properties);
        return this.syncSend(message, timeout);
    }

    /**
     * 异步发送消息
     *
     * @param message
     * @param sendCallback
     */
    public void asyncSend(Message message, SendCallback sendCallback) {
        this.asyncSend(message, sendCallback, producer.getSendMsgTimeout());
    }

    /**
     * @param message
     * @param sendCallback
     * @param timeout
     */
    public void asyncSend(Message message, SendCallback sendCallback, long timeout) {
        try {
            producer.send(message, sendCallback, timeout);
        } catch (Exception e) {
            log.error("send message failed");
            throw new MessagingException(e.getMessage(), e);
        }
    }

    /**
     * 异步发送消息
     *
     * @param topic
     * @param tag
     * @param msgObj
     * @param properties
     * @param sendCallback
     */
    public void asyncSend(String topic, String tag, Object msgObj, Map<String, Object> properties, SendCallback sendCallback) {
        this.asyncSend(topic, tag, msgObj, properties, sendCallback, producer.getSendMsgTimeout());
    }

    /**
     * 异步发送消息
     *
     * @param topic
     * @param tag
     * @param msgObj
     * @param properties
     * @param sendCallback
     * @param timeout
     */
    public void asyncSend(String topic, String tag, Object msgObj, Map<String, Object> properties, SendCallback sendCallback, long timeout) {
        Message message = createMessage(topic, tag, msgObj, properties);
        this.asyncSend(message, sendCallback, timeout);
    }

    /**
     * 不关心消息是否送达
     *
     * @param message
     */
    public void sendOneWay(Message message) {
        try {
            producer.sendOneway(message);
        } catch (Exception e) {
            log.error("send message failed");
            throw new MessagingException(e.getMessage(), e);
        }
    }

    /**
     * 不关心消息是否送达
     *
     * @param topic
     * @param tag
     * @param msgObj
     * @param properties
     */
    public void sendOneWay(String topic, String tag, Object msgObj, Map<String, Object> properties) {
        Message message = createMessage(topic, tag, msgObj, properties);
        sendOneWay(message);
    }

    /**
     * 同步发送到指定队列
     *
     * @param message
     * @param messageQueue
     * @return
     */
    public SendResult syncSend(Message message, MessageQueue messageQueue) {
        return this.syncSend(message, messageQueue, producer.getSendMsgTimeout());
    }

    /**
     * 同步发送到指定队列
     *
     * @param message
     * @param messageQueue
     * @param timeout
     * @return
     */
    public SendResult syncSend(Message message, MessageQueue messageQueue, long timeout) {
        try {
            SendResult sendResult = producer.send(message, messageQueue, timeout);
            return sendResult;
        } catch (Exception e) {
            log.error("send message failed");
            throw new MessagingException(e.getMessage(), e);
        }
    }

    /**
     * 同步发送到指定队列
     *
     * @param topic
     * @param tag
     * @param msgObj
     * @param properties
     * @param mq
     * @return
     */
    public SendResult syncSend(String topic, String tag, Object msgObj, Map<String, Object> properties, MessageQueue mq) {
        return this.syncSend(topic, tag, msgObj, properties, mq, producer.getSendMsgTimeout());
    }

    /**
     * 同步发送到指定队列
     *
     * @param topic
     * @param tag
     * @param msgObj
     * @param properties
     * @param mq
     * @param timeout
     */
    public SendResult syncSend(String topic, String tag, Object msgObj, Map<String, Object> properties, MessageQueue mq, long timeout) {
        Message message = createMessage(topic, tag, msgObj, properties);
        return this.syncSend(message, mq, timeout);
    }

    /**
     * 异步发送到指定队列
     *
     * @param message
     * @param messageQueue
     * @param sendCallback
     */
    public void asyncSend(Message message, MessageQueue messageQueue, SendCallback sendCallback) {
        this.asyncSend(message, messageQueue, sendCallback, producer.getSendMsgTimeout());
    }

    /**
     * 异步发送到指定队列
     *
     * @param message
     * @param messageQueue
     * @param sendCallback
     * @param timeout
     */
    public void asyncSend(Message message, MessageQueue messageQueue, SendCallback sendCallback, long timeout) {
        try {
            producer.send(message, messageQueue, sendCallback, timeout);
        } catch (Exception e) {
            log.error("send message failed");
            throw new MessagingException(e.getMessage(), e);
        }
    }

    /**
     * 异步发送到指定队列
     *
     * @param topic
     * @param tag
     * @param msgObj
     * @param properties
     * @param mq
     * @param sendCallback
     */
    public void asyncSend(String topic, String tag, Object msgObj, Map<String, Object> properties, MessageQueue mq, SendCallback sendCallback) {
        this.asyncSend(topic, tag, msgObj, properties, mq, sendCallback, producer.getSendMsgTimeout());
    }

    /**
     * @param topic
     * @param tag
     * @param msgObj
     * @param properties
     * @param mq
     * @param sendCallback
     * @param timeout
     */
    public void asyncSend(String topic, String tag, Object msgObj, Map<String, Object> properties, MessageQueue mq, SendCallback sendCallback, long timeout) {
        Message message = createMessage(topic, tag, msgObj, properties);
        this.asyncSend(message, mq, sendCallback, timeout);
    }

    /**
     * 不关心是否送达到指定队列
     *
     * @param message
     * @param messageQueue
     */
    public void sendOneWay(Message message, MessageQueue messageQueue) {
        try {
            producer.sendOneway(message, messageQueue);
        } catch (Exception e) {
            log.error("send message failed");
            throw new MessagingException(e.getMessage(), e);
        }
    }

    /**
     * 不关心是否送达到指定队列
     *
     * @param topic
     * @param tag
     * @param msgObj
     * @param properties
     * @param mq
     */
    public void sendOneWay(String topic, String tag, Object msgObj, Map<String, Object> properties, MessageQueue mq) {
        Message message = createMessage(topic, tag, msgObj, properties);
        this.sendOneWay(message, mq);
    }

    /**
     * 同步发送到指定队列
     *
     * @param message
     * @param selector
     * @param arg
     * @return
     */
    public SendResult syncSend(Message message, MessageQueueSelector selector, Object arg) {
        return syncSend(message, selector, arg, producer.getSendMsgTimeout());
    }

    /**
     * 同步发送到指定队列
     *
     * @param message
     * @param selector
     * @param arg
     * @param timeout
     * @return
     */
    public SendResult syncSend(Message message, MessageQueueSelector selector, Object arg, long timeout) {
        try {
            SendResult sendResult = producer.send(message, selector, arg, timeout);
            return sendResult;
        } catch (Exception e) {
            log.error("send message failed");
            throw new MessagingException(e.getMessage(), e);
        }
    }

    /**
     * 同步发送消息到指定队列 （MessageQueueSelector）
     *
     * @param topic
     * @param tag
     * @param msgObj
     * @param properties
     * @param selector
     * @param arg
     * @return
     */
    public SendResult syncSend(String topic, String tag, Object msgObj, Map<String, Object> properties, MessageQueueSelector selector, Object arg) {
        return this.syncSend(topic, tag, msgObj, properties, selector, arg, producer.getSendMsgTimeout());
    }

    /**
     * 同步发送消息到指定队列 （MessageQueueSelector）
     *
     * @param topic
     * @param tag
     * @param msgObj
     * @param properties
     * @param selector
     * @param arg
     * @param timeout
     * @return
     */
    public SendResult syncSend(String topic, String tag, Object msgObj, Map<String, Object> properties, MessageQueueSelector selector, Object arg, long timeout) {
        Message message = createMessage(topic, tag, msgObj, properties);
        return this.syncSend(message, selector, arg, timeout);
    }

    /**
     * 异步发送到指定队列
     *
     * @param message
     * @param selector
     * @param arg
     * @param sendCallback
     */
    public void asyncSend(Message message, MessageQueueSelector selector, Object arg, SendCallback sendCallback) {
        this.asyncSend(message, selector, arg, sendCallback, producer.getSendMsgTimeout());
    }

    /**
     * 异步发送到指定队列
     *
     * @param message
     * @param selector
     * @param arg
     * @param sendCallback
     * @param timeout
     */
    public void asyncSend(Message message, MessageQueueSelector selector, Object arg, SendCallback sendCallback, long timeout) {
        try {
            producer.send(message, selector, arg, sendCallback, timeout);
        } catch (Exception e) {
            log.error("send message failed");
            throw new MessagingException(e.getMessage(), e);
        }
    }

    /**
     * 异步发送消息到指定队列 （MessageQueueSelector）
     *
     * @param topic
     * @param tag
     * @param msgObj
     * @param properties
     * @param selector
     * @param arg
     * @param sendCallback
     */
    public void asyncSend(String topic, String tag, Object msgObj, Map<String, Object> properties, MessageQueueSelector selector, Object arg, SendCallback sendCallback) {
        this.asyncSend(topic, tag, msgObj, properties, selector, arg, sendCallback, producer.getSendMsgTimeout());
    }

    /**
     * 异步发送消息到指定队列 （MessageQueueSelector）
     *
     * @param topic
     * @param tag
     * @param msgObj
     * @param properties
     * @param selector
     * @param arg
     * @param sendCallback
     * @param timeout
     */
    public void asyncSend(String topic, String tag, Object msgObj, Map<String, Object> properties, MessageQueueSelector selector, Object arg, SendCallback sendCallback, long timeout) {
        Message message = createMessage(topic, tag, msgObj, properties);
        this.asyncSend(message, selector, arg, sendCallback, timeout);
    }

    /**
     * 不关心是否送达到指定队列
     *
     * @param message
     * @param selector
     * @param arg
     */
    public void sendOneWay(Message message, MessageQueueSelector selector, Object arg) {
        try {
            producer.sendOneway(message, selector, arg);
        } catch (Exception e) {
            log.error("send message failed");
            throw new MessagingException(e.getMessage(), e);
        }
    }

    /**
     * 不关心是否送达到指定队列
     *
     * @param topic
     * @param tag
     * @param msgObj
     * @param properties
     * @param selector
     * @param arg
     */
    public void sendOneWay(String topic, String tag, Object msgObj, Map<String, Object> properties, MessageQueueSelector selector, Object arg) {
        Message message = createMessage(topic, tag, msgObj, properties);
        this.sendOneWay(message, selector, arg);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(producer, "Property 'producer' is required");
        producer.start();
    }

    @Override
    public void destroy() throws Exception {
        Optional.ofNullable(producer).ifPresent(p -> p.shutdown());
    }

    private Message createMessage(String topic, String tag, Object msgObj, Map<String, Object> properties) {
        Message rocketMsg = new Message(topic, tag, serializer.serialize(msgObj));
        if (!CollectionUtils.isEmpty(properties)) {
            rocketMsg.setFlag((Integer) properties.getOrDefault("FLAG", 0));
            rocketMsg.setWaitStoreMsgOK((Boolean) properties.getOrDefault(MessageConst.PROPERTY_WAIT_STORE_MSG_OK, true));
            Optional.ofNullable((String) properties.get(MessageConst.PROPERTY_KEYS)).ifPresent(keys -> rocketMsg.setKeys(keys));
            Optional.ofNullable((Integer) properties.get(MessageConst.PROPERTY_DELAY_TIME_LEVEL)).ifPresent(delay -> rocketMsg.setDelayTimeLevel(delay));
            Optional.ofNullable((String) properties.get(MessageConst.PROPERTY_BUYER_ID)).ifPresent(buyerId -> rocketMsg.setBuyerId(buyerId));
            properties.entrySet().stream()
                    .filter(entry -> !MessageConst.STRING_HASH_SET.contains(entry.getKey())
                            && !Objects.equals(entry.getKey(), "FLAG"))
                    .forEach(entry -> {
                        rocketMsg.putUserProperty(entry.getKey(), String.valueOf(entry.getValue()));
                    });
        }
        return rocketMsg;
    }
}
