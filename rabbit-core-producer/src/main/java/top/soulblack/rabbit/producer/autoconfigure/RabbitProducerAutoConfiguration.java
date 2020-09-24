package top.soulblack.rabbit.producer.autoconfigure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 自动装配的类
 * Created by lxf on 2020/9/24
 */
@Configuration
@ComponentScan({"top.soulblack.rabbit.producer.*"})
public class RabbitProducerAutoConfiguration {
}
