package kim.sesame.framework.define.sms;

/**
 * 短信操作接口
 */
public interface ISmsService {

    /**
     * 发送短信
     *
     * @param smsEntity 短信实体
     * @return ture/false
     */
    boolean sendMessage(SmsEntity smsEntity);

}
