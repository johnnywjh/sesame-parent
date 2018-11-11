package kim.sesame.framework.rocketmq.msg;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class MsgObject implements java.io.Serializable {
    private String id; // 消息的id,保证不重复就好
    private String msg; // 消息实体的json字符串

    public MsgObject() {
    }

    public MsgObject(String id, String msg) {
        this.id = id;
        this.msg = msg;
    }

}
