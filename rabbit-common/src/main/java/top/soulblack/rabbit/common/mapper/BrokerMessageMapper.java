package top.soulblack.rabbit.common.mapper;

import tk.mybatis.mapper.common.Mapper;
import top.soulblack.rabbit.common.model.BrokerMessage;

import java.util.Date;
import java.util.List;


public interface BrokerMessageMapper extends Mapper<BrokerMessage> {

    List<BrokerMessage> selectFailMessage(String status);

    void updateRetryCount(String messageId, Date updateTime);
}