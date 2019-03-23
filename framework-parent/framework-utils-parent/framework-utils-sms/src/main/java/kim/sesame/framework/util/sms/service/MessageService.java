package kim.sesame.framework.util.sms.service;

import kim.sesame.framework.util.sms.entity.Message;

/**
 * 短信操作接口
 */
public interface MessageService {

    /**
     * 发送短信
     *
     * @param message 短信实体
     * @return ture/false
     */
    boolean sendMessage(Message message);

}
