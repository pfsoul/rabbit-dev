package top.soulblack.rabblit.task.annotation;

import java.lang.annotation.*;

/**
 * Created by lxf on 2020/10/13
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ElasticJobConfig {

    String name(); //elasticjob的名称

    String cron() default "";

    int shardingTotalCount() default 1;

    String shardingItemParameters() default "";

    String jobParameter() default "";

    boolean failover() default false;

    boolean misfire() default true;

    String description() default "";

    boolean overwrite() default false;

    boolean streamingProcess() default false;

    String scriptCommandLine() default "";

    boolean monitorExecution() default false;

    // .... 还有很多很多 详情查看官网文档 apache，用到再加
}