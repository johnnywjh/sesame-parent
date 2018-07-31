package kim.sesame.framework.job;

import com.xxl.job.core.executor.XxlJobExecutor;
import kim.sesame.framework.job.config.XxlJobConfigProperties;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@CommonsLog
@Configuration
@ComponentScan
public class KimJobConfiguration {


    @Autowired
    XxlJobConfigProperties xxlJobConfig;

    @Bean(initMethod = "start", destroyMethod = "destroy")
    public XxlJobExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobExecutor xxlJobExecutor = new XxlJobExecutor();
        xxlJobExecutor.setAdminAddresses(xxlJobConfig.getAdminAddresses());
        xxlJobExecutor.setAppName(xxlJobConfig.getExecutor().getAppName());
        xxlJobExecutor.setIp(xxlJobConfig.getExecutor().getIp());
        xxlJobExecutor.setPort(xxlJobConfig.getExecutor().getPort());
        xxlJobExecutor.setAccessToken(xxlJobConfig.getAccessToken());
        xxlJobExecutor.setLogPath(xxlJobConfig.getExecutor().getLogPath());
        xxlJobExecutor.setLogRetentionDays(xxlJobConfig.getExecutor().getLogRetentionDays());

        return xxlJobExecutor;
    }

}
