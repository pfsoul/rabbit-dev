package top.soulblack.rabbit.api.mq;

import top.soulblack.rabbit.api.config.MessageException;
import top.soulblack.rabbit.api.model.Message;

import java.util.List;

/**
 * Created by lxf on 2020/9/24
 */
public interface MessageProducer {
    // 附带回调
    void send(Message message, SendCallBack sendCallBack) throws MessageException;

    void send(Message message) throws MessageException;

    void send(List<Message> messages) throws MessageException;
}
