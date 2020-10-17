package top.soulblack.rabbit.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;
import top.soulblack.rabbit.task.annotation.EnableElasticJob;

/**
 * Created by lxf on 2020/10/13
 */
// 开启分布式定时任务
@EnableElasticJob
@SpringBootApplication
@MapperScan("top.soulblack.rabbit.common.*")
@ComponentScan("top.soulblack.rabbit.*")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
