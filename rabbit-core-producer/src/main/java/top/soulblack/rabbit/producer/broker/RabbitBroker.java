package top.soulblack.rabbit.producer.broker;

import top.soulblack.rabbit.api.model.Message;

/**
 * Created by lxf on 2020/9/24
 */
public interface RabbitBroker {

    void rapidSend(Message message);

    void confirmSend(Message message);

    void reliantSend(Message message);

    void sendMessages();
}
