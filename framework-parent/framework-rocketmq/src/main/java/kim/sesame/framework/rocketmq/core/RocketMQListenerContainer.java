package kim.sesame.framework.rocketmq.core;

import org.springframework.beans.factory.DisposableBean;


public interface RocketMQListenerContainer extends DisposableBean {

    void setupMessageListener(RocketMQListener<?> messageListener);
}
