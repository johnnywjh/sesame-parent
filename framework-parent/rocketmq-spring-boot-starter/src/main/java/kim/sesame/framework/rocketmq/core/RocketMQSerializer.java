package kim.sesame.framework.rocketmq.core;

/**
 * RocketMQ εΊεε
 **/
public interface RocketMQSerializer {

    byte[] serialize(Object t);


    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
