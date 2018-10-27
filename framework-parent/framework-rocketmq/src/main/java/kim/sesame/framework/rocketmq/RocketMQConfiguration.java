package kim.sesame.framework.rocketmq;


import kim.sesame.framework.rocketmq.core.RocketMQSerializer;
import kim.sesame.framework.rocketmq.core.RocketMQTemplate;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.rocketmq.starter.annotation.EnableRocketMQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

/**
 * RocketMQ 自动装载
 * 官网地址 :  https://github.com/rocketmq/rocketmq-spring-boot-starter
 * 以前的参考项目 : https://github.com/apache/rocketmq-externals/tree/master/rocketmq-spring-boot-starter
 */
@Configuration
@ComponentScan
@EnableRocketMQ  // 启动rockermq
public class RocketMQConfiguration {


    @Autowired
    private RocketMQSerializer serializer;

    @Bean
    @ConditionalOnBean(DefaultMQProducer.class)
    @ConditionalOnMissingBean
    public RocketMQTemplate rocketMQTemplate(DefaultMQProducer mqProducer) {
        RocketMQTemplate rocketMQTemplate = new RocketMQTemplate();
        rocketMQTemplate.setProducer(mqProducer);
        Optional.ofNullable(serializer).ifPresent(s -> rocketMQTemplate.setSerializer(s));
        return rocketMQTemplate;
    }
}
