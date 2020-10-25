package top.soulblack.rabbit.producer.task;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.dataflow.job.DataflowJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import top.soulblack.rabbit.api.model.Message;
import top.soulblack.rabbit.common.model.BrokerMessage;
import top.soulblack.rabbit.producer.broker.RabbitBroker;
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

    @Autowired
    private RabbitBroker rabbitBroker;

    private static final int MAX_RETRY = 3;

    // 抓取的信息
    @Override
    public List<BrokerMessage> fetchData(ShardingContext shardingContext) {
        log.info("执行fetchData");
        List<BrokerMessage> brokerMessages = messageStoreService.selectFailMessage();
        return brokerMessages;
    }

    @Override
    public void processData(ShardingContext shardingContext, List<BrokerMessage> brokerMessages) {
        brokerMessages.forEach(v -> {
            if (v.getTryCount() >= MAX_RETRY) {
                messageStoreService.failure(v.getMessageId());
                log.warn("{} 发送失败且已达重试的最大上限", v.getMessageId());
            } else {
                // 每次重试更新try_count字段
                messageStoreService.updateRetryCount(v.getMessageId());
                // 重试时只需要confirm投递即可
                rabbitBroker.confirmSend(JSONObject.parseObject(v.getMessage(), Message.class));
            }
        });
    }
}
