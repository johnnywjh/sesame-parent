package kim.sesame.common.mq.rocket.templete;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rocketmq.enhance")
@Data
public class RocketEnhanceProperties {

    private boolean enabledIsolation;

    private String environment;
}
