package top.soulblack.rabbit.producer.broker;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.soulblack.rabbit.api.beans.enums.MessageTypeEnum;
import top.soulblack.rabbit.api.model.Message;
import top.soulblack.rabbit.common.enums.BrokerMessageStatusEnum;
import top.soulblack.rabbit.common.model.BrokerMessage;
import top.soulblack.rabbit.producer.config.AsyncBaseQueue;
import top.soulblack.rabbit.producer.serivce.MessageStoreService;

import java.util.Date;
import java.util.List;

/**
 * Created by lxf on 2020/9/24
 */
@Slf4j
@Component
public class RabbitBrokerImpl implements RabbitBroker{

    // 超时时间 1分钟
    private static final Integer TIMEOUT = 1;

    @Autowired
    private RabbitTemplateContainer rabbitTemplateContainer;

    @Autowired
    private MessageStoreService messageStoreService;

    /**
     * 迅速发消息，使用异步线程池
     * @param message 消息
     */
    @Override
    public void rapidSend(Message message) {
        message.setMessageType(MessageTypeEnum.FAST_MESSAGE.name());
        sendKernel(message);
    }

    @Override
    public void confirmSend(Message message) {
        message.setMessageType(MessageTypeEnum.CONFIRM_MESSAGE.name());
        sendKernel(message);
    }

    @Override
    public void reliantSend(Message message) {
        // 消息入库
        BrokerMessage brokerMessage = new BrokerMessage();
        brokerMessage.setMessageId(message.getMessageId());
        brokerMessage.setStatus(BrokerMessageStatusEnum.SENDING.getStatus());
        // tryCount 在最开始发送的时候不需要进行设置
        brokerMessage.setNextRetry(DateUtils.addMinutes(new Date(), TIMEOUT));
        brokerMessage.setCreateTime(new Date());
        brokerMessage.setUpdateTime(new Date());
        brokerMessage.setMessage(message);
        messageStoreService.insert(brokerMessage);

        // 执行真正的发送逻辑
        message.setMessageType(MessageTypeEnum.RELIANT_MESSAGE.name());
        sendKernel(message);
    }

    @Override
    public void sendMessages() {
        List<Message> messages = MessageHolder.clear();
        messages.forEach(message -> {
            AsyncBaseQueue.submit((Runnable)() ->{
                CorrelationData correlationData = new CorrelationData(String.format("%s#%s#%s", message.getMessageId(), System.currentTimeMillis(), message.getMessageType()));
                RabbitTemplate rabbitTemplate = rabbitTemplateContainer.getTemplate(message);
                rabbitTemplate.convertAndSend(
                        message.getTopic(),
                        message.getRoutingKey(),
                        message,
                        correlationData
                );
                log.info("#RabbitBrokerImpl.sendMessages# : rabbitMQ Id:{}", message.getMessageId());
            });
        });
    }

    /**
     * 发送消息的核心方法，不向外暴露
     * @param message 消息实体类
     */
    private void sendKernel(Message message) {
        AsyncBaseQueue.submit((Runnable)() ->{
            CorrelationData correlationData = new CorrelationData(String.format("%s#%s#%s", message.getMessageId(), System.currentTimeMillis(), message.getMessageType()));
            RabbitTemplate rabbitTemplate = rabbitTemplateContainer.getTemplate(message);
            rabbitTemplate.convertAndSend(
                    message.getTopic(),
                    message.getRoutingKey(),
                    message,
                    correlationData
            );
            log.info("#RabbitBrokerImpl.sendKernel# : rabbitMQ Id:{}", message.getMessageId());
        });
    }
}
