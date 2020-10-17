package top.soulblack.rabbit.producer.task;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.dataflow.job.DataflowJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import top.soulblack.rabbit.common.model.BrokerMessage;
import top.soulblack.rabbit.producer.serivce.MessageStoreService;
import top.soulblack.rabbit.task.annotation.ElasticJobConfig;

import java.util.List;

/**
 * Created by lxf on 2020/10/14
 */
@ElasticJobConfig(
        name = "top.soulblack.rabbit.producer.task.RetryMessage",
        nickName = "retryMessage",
        description = "可靠性投递消息补偿任务",
        overwrite = true,
        cron = "0/10 * * * * ?",
        shardingTotalCount = 1
)
@Slf4j
@Component
public class RetryMessage implements DataflowJob<BrokerMessage> {

    @Autowired
    private MessageStoreService messageStoreService;

    // 抓取的信息
    @Override
    public List<BrokerMessage> fetchData(ShardingContext shardingContext) {
        log.info("执行fetchData");
        List<BrokerMessage> brokerMessages = messageStoreService.selectFailMessage();
        return null;
    }

    @Override
    public void processData(ShardingContext shardingContext, List list) {

    }
}
