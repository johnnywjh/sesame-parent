package kim.sesame.framework.rocketmq;


import org.rocketmq.starter.annotation.EnableRocketMQ;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * RocketMQ 自动装载
 * 官网地址 :  https://github.com/rocketmq/rocketmq-spring-boot-starter
 * 以前的参考项目 : https://github.com/apache/rocketmq-externals/tree/master/rocketmq-spring-boot-starter
 */
@Configuration
@ComponentScan
@EnableRocketMQ  // 启动rockermq
public class RocketMQAutoConfiguration {

}
