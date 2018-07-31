package kim.sesame.framework.job.config;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * xxl-job config
 *
 * @author xuxueli 2017-04-28
 */
@Data
@Component
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobConfigProperties {
    private Logger logger = LoggerFactory.getLogger(XxlJobConfigProperties.class);

    private String adminAddresses;
    private String accessToken;
    private Executor executor;

    @Data
    public static class Executor {
        private String appName;
        private String ip;
        private int port;
        private String logPath;
        private int logRetentionDays;
    }


}