package top.soulblack.rabbit.common.serializer;

/**
 * Created by lxf on 2020/9/29
 * 序列化与反序列化的接口
 */
public interface Serializer {

    byte[] serializeRaw(Object data);

    String serialize(Object data);

    <T> T deserialize(String content);

    <T> T deserialize(byte[] content);

}
