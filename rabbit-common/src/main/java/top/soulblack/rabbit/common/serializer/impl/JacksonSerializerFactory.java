package top.soulblack.rabbit.common.serializer.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import top.soulblack.rabbit.api.model.Message;
import top.soulblack.rabbit.common.serializer.Serializer;
import top.soulblack.rabbit.common.serializer.SerializerFactory;

/**
 * Created by lxf on 2020/9/29
 */
@Component
public class JacksonSerializerFactory implements SerializerFactory {


    public static final SerializerFactory INSTANCE = new JacksonSerializerFactory();

    @Override
    @Bean
    public Serializer create() {
        return JacksonSerializer.createParametricType(Message.class);
    }
}
