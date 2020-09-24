package top.soulblack.rabbit.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.soulblack.rabbit.api.beans.enums.MessageTypeEnum;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lxf on 2020/9/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {

    private static final long serialVersionUID = 3072013121237435682L;

    // 唯一的消息Id
    private String messageId;

    // rabbitmq中的exchange
    private String topic;

    // 消息的路由规则
    private String routingKey;

    // 消息的附加属性
    private Map<String, Object> attributes = new HashMap<>();

    // 贪吃消息的参数配置，即延迟时间
    private Integer delayMills;

    // 消息的类型 默认为confirm消息
    private String messageType = MessageTypeEnum.CONFIRM_MESSAGE.name();
}
