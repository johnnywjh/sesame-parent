package kim.sesame.framework.websocket.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * websocket 配置
 * 当给用户推送消息时,如果用户的会话Session不是保存在本机时,这是需要通过http请求放到到对应的实例, 然后进行推送
 **/
@Data
@Component
@ConfigurationProperties(prefix = "sesame.framework.ws")
public class WebSocketProperties implements InitializingBean {
    /**
     * 尝试次数, 默认5次
     */
    private int totalCount = 5;

    /**
     * 每一次等待时间,单位秒, 默认1分钟
     */
    private int failRetryTime = 60;

    @Override
    public void afterPropertiesSet() throws Exception {
        WebSocketConfig.setTotalCount(this.totalCount);
        WebSocketConfig.setFailRetryTime(this.failRetryTime);
    }
}
