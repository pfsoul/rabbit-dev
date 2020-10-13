package top.soulblack.rabbit.producer.serivce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.soulblack.rabbit.common.enums.BrokerMessageStatusEnum;
import top.soulblack.rabbit.common.mapper.BrokerMessageMapper;
import top.soulblack.rabbit.common.model.BrokerMessage;

import java.util.Date;

/**
 * Created by lxf on 2020/10/12
 */
@Service
public class MessageStoreService {

    @Autowired
    private BrokerMessageMapper brokerMessageMapper;

    public void insert(BrokerMessage brokerMessage) {
        brokerMessageMapper.insert(brokerMessage);
    }

    public void success(String messageId) {
        this.changeStatus(messageId, BrokerMessageStatusEnum.SEND_OK.getStatus());
    }

    public void failure(String messageId) {
        this.changeStatus(messageId, BrokerMessageStatusEnum.SEND_FAIL.getStatus());
    }

    /**
     * 改变状态通用方法
     * @param messageId message主键
     * @param status 状态码
     */
    public void changeStatus(String messageId, String status) {
        BrokerMessage brokerMessage = new BrokerMessage();
        brokerMessage.setMessageId(messageId);
        brokerMessage.setStatus(status);
        brokerMessage.setUpdateTime(new Date());
        brokerMessageMapper.updateByPrimaryKeySelective(brokerMessage);
    }
}
