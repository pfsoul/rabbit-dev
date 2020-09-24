package top.soulblack.rabbit.api.utils;

import top.soulblack.rabbit.api.beans.enums.MessageTypeEnum;
import top.soulblack.rabbit.api.config.MessageException;
import top.soulblack.rabbit.api.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lxf on 2020/9/24
 * 建造者模式
 */
public class MessageBuilder {

    // 唯一的消息Id
    private String messageId;

    // rabbitmq中的exchange
    private String topic;

    // 消息的路由规则
    private String routingKey;

    // 消息的附加属性
    private final Map<String, Object> attributes = new ConcurrentHashMap<>();

    // 贪吃消息的参数配置，即延迟时间
    private Integer delayMills;

    // 消息的类型 默认为confirm消息
    private String messageType = MessageTypeEnum.CONFIRM_MESSAGE.name();

    MessageBuilder() {

    }

    public static MessageBuilder builder() {
        return new MessageBuilder();
    }

    public MessageBuilder withMessageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public MessageBuilder withTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public MessageBuilder withRoutingKey(String routingKey) {
        this.routingKey = routingKey;
        return this;
    }

    public MessageBuilder withAttributes(String key, Object value) {
        this.attributes.put(key, value);
        return this;
    }

    public MessageBuilder withDelayMills(Integer delayMills) {
        this.delayMills = delayMills;
        return this;
    }

    public MessageBuilder withMessageType(String messageType) {
        this.messageType = messageType;
        return this;
    }


    public Message build() throws Exception {
        if (messageId == null) {
            messageId = UUID.randomUUID().toString();
        }
        if (topic == null) {
            throw new MessageException("topic不能为空");
        }
        return new Message(messageId, topic, routingKey, attributes, delayMills, messageType);
    }
}
