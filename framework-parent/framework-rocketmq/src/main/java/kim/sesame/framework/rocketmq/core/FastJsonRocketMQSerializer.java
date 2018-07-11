package kim.sesame.framework.rocketmq.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.IOUtils;

/**
 * FastJson 序列化
 **/
public class FastJsonRocketMQSerializer implements RocketMQSerializer{

    @Override
    public byte[] serialize(Object t) {
        return JSON.toJSONBytes(t);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(new String(bytes, IOUtils.UTF8), clazz);
    }
}
