package top.soulblack.rabbit.common.serializer.impl;

import top.soulblack.rabbit.api.model.Message;
import top.soulblack.rabbit.common.serializer.Serializer;
import top.soulblack.rabbit.common.serializer.SerializerFactory;

/**
 * Created by lxf on 2020/9/29
 */
public class JacksonSerializerFactory implements SerializerFactory {


    public static final SerializerFactory INSTANCE = new JacksonSerializerFactory();

    @Override
    public Serializer create() {
        return JacksonSerializer.createParametricType(Message.class);
    }
}
