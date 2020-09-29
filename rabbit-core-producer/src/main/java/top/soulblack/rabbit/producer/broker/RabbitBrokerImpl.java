package top.soulblack.rabbit.producer.broker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.soulblack.rabbit.api.beans.enums.MessageTypeEnum;
import top.soulblack.rabbit.api.model.Message;
import top.soulblack.rabbit.producer.config.AsyncBaseQueue;

/**
 * Created by lxf on 2020/9/24
 */
@Slf4j
@Component
public class RabbitBrokerImpl implements RabbitBroker{

    @Autowired
    private RabbitTemplateContainer rabbitTemplateContainer;

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
        message.setMessageType(MessageTypeEnum.RELIANT_MESSAGE.name());
        sendKernel(message);
    }

    /**
     * 发送消息的核心方法，不向外暴露
     * @param message 消息实体类
     */
    private void sendKernel(Message message) {
        AsyncBaseQueue.submit((Runnable)() ->{
            CorrelationData correlationData = new CorrelationData(String.format("%s#%s", message.getMessageId(), System.currentTimeMillis()));
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
