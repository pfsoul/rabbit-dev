package top.soulblack.rabbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import top.soulblack.rabbit.task.annotation.EnableElasticJob;

/**
 * Created by lxf on 2020/10/20
 */

@SpringBootApplication
@ComponentScan({"top.soulblack.rabbit.*"})
@EnableElasticJob
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
