package kim.sesame.framework.define.sms;

import kim.sesame.framework.define.notice.NoticeEntity;
import lombok.Data;

import java.util.Date;

@Data
public class SmsEntity extends NoticeEntity {

    private String phone;//手机号
    private String templateCode;//模板编号
    private String templateParam;//模板参数,发送内容
    private Date sendTime;//发送时间

    public SmsEntity() {
    }

    public SmsEntity(String phone) {
        this.phone = phone;
    }

    public SmsEntity(String phone, String templateCode, String templateParam) {
        this.phone = phone;
        this.templateCode = templateCode;
        this.templateParam = templateParam;
        this.sendTime = new Date();
    }

    public SmsEntity(String phone, String templateCode, String templateParam, Date sendTime) {
        this.phone = phone;
        this.templateCode = templateCode;
        this.templateParam = templateParam;
        this.sendTime = sendTime;
    }
}