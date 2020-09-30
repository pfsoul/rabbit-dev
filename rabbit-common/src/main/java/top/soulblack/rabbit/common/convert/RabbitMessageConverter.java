package top.soulblack.rabbit.common.convert;

import com.google.common.base.Preconditions;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;


/**
 * Created by lxf on 2020/9/30
 * 装饰者模式或静态代理
 */
public class RabbitMessageConverter implements MessageConverter {

    private GenericMessageConverter genericMessageConverter;

    private final String defaultExpire = String.valueOf(24 * 60 * 60 * 1000);

    public RabbitMessageConverter(GenericMessageConverter genericMessageConverter) {
        Preconditions.checkNotNull(genericMessageConverter);
        this.genericMessageConverter = genericMessageConverter;
    }

    @Override
    public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
        // 装饰者模式过期时间
        messageProperties.setExpiration(defaultExpire);
        return this.genericMessageConverter.toMessage(o, messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {

        return (top.soulblack.rabbit.api.model.Message) this.genericMessageConverter.fromMessage(message);
    }
}
