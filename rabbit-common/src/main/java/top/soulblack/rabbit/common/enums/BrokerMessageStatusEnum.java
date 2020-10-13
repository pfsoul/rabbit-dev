package top.soulblack.rabbit.common.enums;

/**
 * Created by lxf on 2020/10/12
 */
public enum BrokerMessageStatusEnum {

    SENDING("0", "发送中"),

    SEND_OK("1", "发送成功"),

    SEND_FAIL("2", "发送失败");

    private String status;

    private String decr;

    BrokerMessageStatusEnum(String status, String decr) {
        this.status = status;
        this.decr = decr;
    }

    public String getStatus() {
        return status;
    }

    public String getDecr() {
        return decr;
    }
}

