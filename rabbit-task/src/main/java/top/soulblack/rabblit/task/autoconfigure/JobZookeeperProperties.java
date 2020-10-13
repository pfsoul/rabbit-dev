package top.soulblack.rabblit.task.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by lxf on 2020/10/13
 */
@Data
@ConfigurationProperties(prefix = "elastic.job.zk")
public class JobZookeeperProperties {

    private String namespace;

    private String serverLists;

    private Integer maxRetries = 3;

    // 秒为单位
    private Integer connectionTimeoutMilliseconds = 10000;

    // 连接超时时间
    private Integer sessionTimeoutMilliseconds = 60000;

    private Integer baseSleepTimeMilliseconds = 1000;

    private Integer maxSleepTimeMilliseconds = 3000;

    private String digest = "";

    // https://shardingsphere.apache.org/elasticjob/current/en/user-manual/elasticjob-lite/configuration/
}
