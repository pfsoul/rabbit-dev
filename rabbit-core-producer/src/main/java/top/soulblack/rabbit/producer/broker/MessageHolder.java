package top.soulblack.rabbit.producer.broker;

import org.springframework.stereotype.Component;
import top.soulblack.rabbit.api.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lxf on 2020/10/20
 */
public class MessageHolder {

    public List<Message> messages = new ArrayList<>();

    public static final ThreadLocal<MessageHolder> holder = new ThreadLocal<MessageHolder>(){
        @Override
        protected MessageHolder initialValue() {
            return new MessageHolder();
        }
    };

    public static void add(Message message) {
        holder.get().messages.add(message);
    }

    public static List<Message> clear() {
        List<Message> tmp = new ArrayList<>(holder.get().messages);
        holder.remove();
        return tmp;
    }
}
