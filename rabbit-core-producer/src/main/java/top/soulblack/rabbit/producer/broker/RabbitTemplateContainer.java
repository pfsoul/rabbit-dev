package top.soulblack.rabbit.producer.broker;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import top.soulblack.rabbit.api.beans.enums.MessageTypeEnum;
import top.soulblack.rabbit.api.config.MessageException;
import top.soulblack.rabbit.api.model.Message;

import top.soulblack.rabbit.common.convert.GenericMessageConverter;
import top.soulblack.rabbit.common.convert.RabbitMessageConverter;
import top.soulblack.rabbit.common.serializer.Serializer;
import top.soulblack.rabbit.common.serializer.SerializerFactory;
import top.soulblack.rabbit.common.serializer.impl.JacksonSerializerFactory;
import top.soulblack.rabbit.producer.serivce.MessageStoreService;

import java.util.List;
import java.util.Map;

/**
 * Created by lxf on 2020/9/29
 * 1. 每一个topic对应一个template，提高发送效率
 * 2. 可以根据不同需求指定化不同的template,比如每个topic都有自己的routingKey规则
 */
@Slf4j
@Component
public class RabbitTemplateContainer implements RabbitTemplate.ConfirmCallback {

    private Map<String /* topic */, RabbitTemplate> rabbitMap = Maps.newConcurrentMap();

    private SerializerFactory serializerFactory = JacksonSerializerFactory.INSTANCE;

    private Splitter splitter = Splitter.on("#");

    @Autowired
    private MessageStoreService messageStoreService;

    // amqp 包
    @Autowired
    private ConnectionFactory connectionFactory;

    public RabbitTemplate getTemplate(Message message) throws MessageException {
        Preconditions.checkNotNull(message);
        String topic = message.getTopic();
        RabbitTemplate rabbitTemplate = rabbitMap.get(topic);
        if (rabbitTemplate != null) {
            return rabbitTemplate;
        }
        log.info("#RabbitTemplateContainer:topic {} is not exists, create one", topic);
        RabbitTemplate newTemplate = new RabbitTemplate(connectionFactory);
        newTemplate.setExchange(topic);
        newTemplate.setRetryTemplate(new RetryTemplate());
        newTemplate.setRoutingKey(message.getRoutingKey());

        // 对于message的序列化方式 添加序列化和反序列化对象
        Serializer serializer = serializerFactory.create();
        GenericMessageConverter genericMessageConverter = new GenericMessageConverter(serializer);
        RabbitMessageConverter rabbitMessageConverter = new RabbitMessageConverter(genericMessageConverter);
        newTemplate.setMessageConverter(rabbitMessageConverter);

        if (!MessageTypeEnum.FAST_MESSAGE.name().equals(message.getMessageType())) {
            newTemplate.setConfirmCallback(this);
            //事务设置：newTemplate.setChannelTransacted(true);
        }
        // 不存在则put
        rabbitMap.putIfAbsent(topic, newTemplate);
        return rabbitMap.get(topic);
    }

    // 回调确认方法 无论是confirm消息还是reliant消息，都会回调confirm
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        // 具体的消息应答
        List<String> strings = splitter.splitToList(correlationData.getId());
        String messageId = strings.get(0);
        Long sendTime = Long.parseLong(strings.get(1));
        String messageType = strings.get(2);
        // 成功返回结果
        if (ack) {
            // 当broker返回ack成功是，更新一下日志里对应的消息发送状态为SendOK

            // 如果当前消息为reliant消息，则更新状态
            if (messageType.equals(MessageTypeEnum.RELIANT_MESSAGE.name())) {
                this.messageStoreService.success(messageId);
            }
            log.info("send message is OK, confirm messageId:{}, sendTime:{}", messageId, sendTime);
        } else {
            log.warn("send message is FAIL, confirm messageId:{}, sendTime:{}", messageId, sendTime);
        }
    }
}

