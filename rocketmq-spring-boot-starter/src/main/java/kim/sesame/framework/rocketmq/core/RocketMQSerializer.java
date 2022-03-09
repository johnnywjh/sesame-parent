package kim.sesame.framework.rocketmq.core;

/**
 * RocketMQ 序列化
 **/
public interface RocketMQSerializer {

    byte[] serialize(Object t);


    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
