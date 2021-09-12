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

    /**
     * 发送短信验证码
     * @param phone 手机号,必填
     * @param time 失效时间,可为空
     * @return  true/false
     */
    boolean sendVifCode(String phone, Integer time);

    /**
     * 校验短信验证码
     * @param phone 手机号,必填
     * @param vifCode 验证码,必填
     * @return  true/false
     */
    boolean checkSmsVifCode(String phone, String vifCode);
}
