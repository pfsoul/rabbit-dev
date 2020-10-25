package top.soulblack.rabbit.receive;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import top.soulblack.rabbit.common.convert.GenericMessageConverter;
import top.soulblack.rabbit.common.serializer.impl.JacksonSerializer;


import java.io.IOException;
import java.util.Map;

/**
 * Created by lxf on 2020/10/20
 */
@Slf4j
@Component
public class RabbitConsumer {

    private GenericMessageConverter genericMessageConverter;

    /**
     * RabbitHandler注解，通知SpringBoot该方法用于接收消息,这个方法运行后将处于等待状态，
     * 有新的消息进来就会自动触发该方法处理消息
     *
     *  @param message   Payload将接收的消息反序列化后注入到message对象
     *  @param channel   用于接收消息后进行Ack处理
     *  @param headers   headers获取辅助描述信息
     */
    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            // 队列信息，当队列不存在时会自动创建一个队列，并绑定下面交换机 routing-key
            value = @Queue(value = "springboot.rabbit", durable = "true"),
            // 交换机信息，即topic
            exchange = @Exchange(value = "exchange-1", durable = "true", type = "topic"),
            ignoreDeclarationExceptions = "true",
            // 路由规则 即发送时的
            key = "springboot.*"
    ))
    public void receiveMessage(Message message, Channel channel, @Headers Map<String, Object> headers) {
        genericMessageConverter = new GenericMessageConverter(JacksonSerializer.createParametricType(top.soulblack.rabbit.api.model.Message.class));
        log.info("message : {}",
                JSON.toJSONString(genericMessageConverter.fromMessage(message)));
        long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        try {
            // 第一个参数 tag
            // 第二个参数，是否批量接收
            // 该应答只能改变queue中是否删除该消息，在ConfirmCallback中投递exchange成功则为true，不因此而改变
            channel.basicAck(tag, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("=========================");
    }


}
