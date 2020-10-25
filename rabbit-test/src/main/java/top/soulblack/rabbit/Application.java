package top.soulblack.rabbit;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;
import top.soulblack.rabbit.task.annotation.EnableElasticJob;

/**
 * Created by lxf on 2020/10/20
 */

@SpringBootApplication
@EnableRabbit
@ComponentScan({"top.soulblack.rabbit.*"})
@EnableElasticJob
@MapperScan("top.soulblack.rabbit.common.*")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
