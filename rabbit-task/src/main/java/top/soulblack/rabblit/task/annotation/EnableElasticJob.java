package top.soulblack.rabblit.task.annotation;

import org.springframework.context.annotation.Import;
import top.soulblack.rabblit.task.autoconfigure.JobParserAutoConfiguration;

import java.lang.annotation.*;

/**
 * Created by lxf on 2020/10/13
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(JobParserAutoConfiguration.class)
public @interface EnableElasticJob {

}
