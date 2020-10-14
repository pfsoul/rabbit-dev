package top.soulblack.rabbit.producer.task;

import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.dataflow.job.DataflowJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.soulblack.rabbit.api.beans.enums.MessageTypeEnum;
import top.soulblack.rabbit.api.model.Message;
import top.soulblack.rabbit.common.enums.BrokerMessageStatusEnum;
import top.soulblack.rabbit.common.model.BrokerMessage;
import top.soulblack.rabbit.producer.serivce.MessageStoreService;
import top.soulblack.rabblit.task.annotation.ElasticJobConfig;

import java.util.List;

/**
 * Created by lxf on 2020/10/14
 */
@ElasticJobConfig(
        name = "top.soulblack.rabbit.producer.task.RetryMessage",
        description = "可靠性头题消息补偿任务",
        overwrite = true,
        cron = "0/10 * * * * ?",
        shardingTotalCount = 1
)
@Component
public class RetryMessage implements DataflowJob<BrokerMessage> {

    @Autowired
    private MessageStoreService messageStoreService;

    // 抓取的信息
    @Override
    public List<BrokerMessage> fetchData(ShardingContext shardingContext) {
        List<BrokerMessage> brokerMessages = messageStoreService.selectFailMessage();
        return null;
    }

    @Override
    public void processData(ShardingContext shardingContext, List list) {

    }
}
