package kim.sesame.framework.rocketmq.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.IOUtils;
import org.springframework.stereotype.Component;

/**
 * FastJson 序列化
 **/
@Component
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
