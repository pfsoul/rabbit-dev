package top.soulblack.rabbit.producer.autoconfigure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import top.soulblack.rabbit.task.annotation.EnableElasticJob;

/**
 * 自动装配的类
 * Created by lxf on 2020/9/24
 */
@Configuration
@EnableElasticJob
@ComponentScan({"top.soulblack.rabbit.producer.*"})
public class RabbitProducerAutoConfiguration {
}
