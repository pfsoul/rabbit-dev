package top.soulblack.rabbit.producer.broker;

import com.google.common.base.Preconditions;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import top.soulblack.rabbit.api.beans.enums.MessageTypeEnum;
import top.soulblack.rabbit.api.config.MessageException;
import top.soulblack.rabbit.api.model.Message;
import top.soulblack.rabbit.api.mq.MessageProducer;
import top.soulblack.rabbit.api.mq.SendCallBack;

import java.util.List;


/**
 * Created by lxf on 2020/9/24
 */
@Component
public class ProducerClient implements MessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitBroker rabbitBroker;

    @Override
    public void send(Message message, SendCallBack sendCallBack) throws MessageException {

    }

    @Override
    public void send(Message message) throws MessageException {
        Preconditions.checkNotNull(message.getTopic());
        String messageType = message.getMessageType();
        MessageTypeEnum typeEnum = MessageTypeEnum.getEnumByStr(messageType);
        Preconditions.checkNotNull(typeEnum);
        switch (typeEnum) {
            case FAST_MESSAGE:
                rabbitBroker.rapidSend(message);
                break;
            case CONFIRM_MESSAGE:
                rabbitBroker.confirmSend(message);
                break;
            case RELIANT_MESSAGE:
                rabbitBroker.reliantSend(message);
                break;
            default:
                break;
        }
    }

    /**
     * 假如批量发送message，则设置为快速消息
     */
    @Override
    public void send(List<Message> messages) throws MessageException {
        if (CollectionUtils.isEmpty(messages)) {
            return;
        }
        messages.forEach(message -> {
            message.setMessageType(MessageTypeEnum.FAST_MESSAGE.name());
            MessageHolder.add(message);
        });
        rabbitBroker.sendMessages();
    }
}
