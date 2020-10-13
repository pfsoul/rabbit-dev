package top.soulblack.rabblit.task.parser;

import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperRegistryCenter;
import top.soulblack.rabblit.task.autoconfigure.JobZookeeperProperties;

/**
 * Created by lxf on 2020/10/13
 */
public class ElasticJobConfParser {

    private JobZookeeperProperties jobZookeeperProperties;

    private ZookeeperRegistryCenter zookeeperRegistryCenter;

    public ElasticJobConfParser(JobZookeeperProperties jobZookeeperProperties, ZookeeperRegistryCenter zookeeperRegistryCenter) {
        this.jobZookeeperProperties = jobZookeeperProperties;
        this.zookeeperRegistryCenter = zookeeperRegistryCenter;
    }
}
