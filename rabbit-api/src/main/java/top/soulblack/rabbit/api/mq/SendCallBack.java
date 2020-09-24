package top.soulblack.rabbit.api.mq;

/**
 * Created by lxf on 2020/9/24
 */
public interface SendCallBack {

    void onSuccess();

    void onFailure();
}
