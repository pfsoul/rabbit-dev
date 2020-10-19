package top.soulblack.rabbit.common.model;

import top.soulblack.rabbit.api.model.Message;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "broker_message")
public class BrokerMessage {
    @Id
    @Column(name = "message_id")
    @GeneratedValue(generator = "JDBC")
    private String messageId;

    private Message message;

    @Column(name = "try_count")
    private Integer tryCount;

    private String status;

    @Column(name = "next_retry")
    private Date nextRetry;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * @return message_id
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * @param messageId
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId == null ? null : messageId.trim();
    }

    /**
     * @return message
     */
    public Message getMessage() {
        return message;
    }

    /**
     * @param message
     */
    public void setMessage(Message message) {
        this.message = message;
    }

    /**
     * @return try_count
     */
    public Integer getTryCount() {
        return tryCount;
    }

    /**
     * @param tryCount
     */
    public void setTryCount(Integer tryCount) {
        this.tryCount = tryCount;
    }

    /**
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * @return next_retry
     */
    public Date getNextRetry() {
        return nextRetry;
    }

    /**
     * @param nextRetry
     */
    public void setNextRetry(Date nextRetry) {
        this.nextRetry = nextRetry;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}