package kim.sesame.framework.email.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * email 邮件配置
 **/
@Data
@Component
@ConfigurationProperties(prefix = "spring.mail")
public class EmailProperties {

    /**
     * 邮件的发送人
     */
    private String username;

    /**
     * 邮件发送的昵称
     */
    private String sendNickname;

}
