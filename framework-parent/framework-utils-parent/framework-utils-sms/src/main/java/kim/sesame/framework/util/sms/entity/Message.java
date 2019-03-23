package kim.sesame.framework.util.sms.entity;

import kim.sesame.framework.util.sms.define.SmsPlatform;
import lombok.Data;

import java.util.Date;

@Data
public class Message implements java.io.Serializable {

    private String account;//账号
    private String phone;//手机号
    private SmsPlatform platform;//平台
    private String templateCode;//模板编号
    private String templateParam;//模板参数,发送内容
    private Date sendTime;//发送时间

    public Message() {
    }

    public Message(String account, String phone, SmsPlatform platform, String templateCode, String templateParam) {
        this.account = account;
        this.phone = phone;
        this.platform = platform;
        this.templateCode = templateCode;
        this.templateParam = templateParam;
        this.sendTime = new Date();
    }

    public Message(String account, String phone, SmsPlatform platform, String templateCode, String templateParam, Date sendTime) {
        this.account = account;
        this.phone = phone;
        this.platform = platform;
        this.templateCode = templateCode;
        this.templateParam = templateParam;
        this.sendTime = sendTime;
    }
}
