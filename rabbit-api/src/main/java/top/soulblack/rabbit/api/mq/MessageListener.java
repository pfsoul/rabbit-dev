package top.soulblack.rabbit.api.mq;

import top.soulblack.rabbit.api.model.Message;

/**
 * Created by lxf on 2020/9/24
 */
public interface MessageListener {

    void onMessage(Message message);
}
