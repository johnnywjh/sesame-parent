package kim.sesame.framework.websocket.entity;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

/**
 * layim 消息实体类
 */
@Data
public class LayImMsg implements java.io.Serializable {

    private String id;// 主键
    private String sendId; //发送人id
    private String sendName;//发送人姓名
    private String sendImg;//发送人头像

    private String content;// 消息内容
    private String toId;// 接受人id
    private Date createTime; //渐渐时间,发送时间

    public LayImMsg() {
        id = UUID.randomUUID().toString().replace("-", "");
        createTime = new Date();
    }

    public String getJoin() {
        return sendId + "#" + toId;
    }

}
