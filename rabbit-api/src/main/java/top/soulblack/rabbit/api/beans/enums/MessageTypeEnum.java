package top.soulblack.rabbit.api.beans.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by lxf on 2020/9/24
 */
public enum MessageTypeEnum {

    // 迅速消息，不需要保障消息的可靠性，也不需要confirm确认
    FAST_MESSAGE,
    // 确认消息，需要收到相关回调
    CONFIRM_MESSAGE,
    // 可靠性消息，一定要保障消息的100%可靠性投递，不允许有任何消息的丢失
    // 保障数据库和所发的消息原子性（最终一致）
    RELIANT_MESSAGE;
    // 延迟消息，延迟一段时间发送该消息
    // DELAY_MESSAGE

    public static MessageTypeEnum getEnumByStr(String messageType) {
        if (StringUtils.isBlank(messageType)) {
            return null;
        }
        for (MessageTypeEnum typeEnum : MessageTypeEnum.values()) {
            if (typeEnum.name().equals(messageType)) {
                return typeEnum;
            }
        }
        return null;
    }

}
