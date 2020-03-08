package kim.sesame.framework.rocketmq.msg;

import com.alibaba.fastjson.JSON;
import com.zengtengpeng.annotation.Lock;
import kim.sesame.framework.utils.Argument;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 保证消费端不重复消费
 */
@Service
@CommonsLog
public class MsgConsumerService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 默认消费者的名称
     */
    private final static String DEFAULT_CONSUMER_GROUP = "DEFAULT_GROUP";

    /**
     * 默认消息消费只有的保存时间, 3天
     */
    private final static long DEFAULT_KEY_TIME = 3 * 24;

    /**
     * 采用默认消费组和默认保存时间消费  不重复的消费数据
     *
     * @param msgObject 从mq接受的的消息,MsgObject 类型的字符串
     * @param consumer  消费的逻辑代码
     */
    public void msgConsole(String msgObject, Consumer<String> consumer) {
        msgConsole(msgObject, DEFAULT_CONSUMER_GROUP, DEFAULT_KEY_TIME, consumer);
    }

    /**
     * 指定默认消费组  但采用默认保存时间消费  不重复的消费数据
     *
     * @param msgObject         从mq接受的的消息,MsgObject 类型的字符串
     * @param consumerGroupName 消费组的名称
     * @param consumer          消费的逻辑代码
     */
    public void msgConsole(String msgObject, String consumerGroupName, Consumer<String> consumer) {
        msgConsole(msgObject, consumerGroupName, DEFAULT_KEY_TIME, consumer);
    }

    /**
     * 采用默认消费组,  但指定 消息保存时间
     *
     * @param msgObject 从mq接受的的消息,MsgObject 类型的字符串
     * @param keyTime   消息消费之后,保存在redis中的时间,单位小时
     * @param consumer  消费的逻辑代码
     */
    public void msgConsole(String msgObject, long keyTime, Consumer<String> consumer) {
        msgConsole(msgObject, DEFAULT_CONSUMER_GROUP, keyTime, consumer);
    }

    /**
     * 不重复的消费数据
     *
     * @param msgObject         从mq接受的的消息,MsgObject 类型的字符串
     * @param consumerGroupName 消费组的名称
     * @param keyTime           消息消费之后,保存在redis中的时间,单位小时
     * @param consumer          消费的逻辑代码
     */
    public void msgConsole(String msgObject, String consumerGroupName, long keyTime, Consumer<String> consumer) {
        Argument.notEmpty(msgObject, "消息不能为空");
        Argument.notEmpty(consumerGroupName, "消费者组名称不能为空");

        MsgObject mo = JSON.parseObject(msgObject, MsgObject.class);
        if (mo == null) {
            log.debug("转化失败,请按照 MsgObject类型传过来" + msgObject);
            return;
        }

        // 1. 判断消息有没有被消费过
        String lockKey = getRedisKey(mo.getId(), consumerGroupName);
        String redisKey = "redis_" + lockKey;
        String result = stringRedisTemplate.opsForValue().get(redisKey);

        String name = Thread.currentThread().getName();
        if (StringUtils.isEmpty(result)) {
            try {
                lock(lockKey, consumer, mo, redisKey, keyTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Lock(keys = "#key", keyConstant = "_redis")
    public void lock(String key, Consumer<String> consumer, MsgObject mo, String redisKey, long keyTime) {
        //3 消费消息
        consumer.accept(mo.getMsg());

        // 4 往reids中存入消息
        stringRedisTemplate.opsForValue().set(redisKey, "ok", keyTime, TimeUnit.HOURS);
    }

    private String getRedisKey(String msgObject, String consumerGroupName) {
        return "mq_consumer_console_" + consumerGroupName + '_' + msgObject;
    }
}
