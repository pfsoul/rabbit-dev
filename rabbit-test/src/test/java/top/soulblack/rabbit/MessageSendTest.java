package top.soulblack.rabbit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.soulblack.rabbit.api.beans.enums.MessageTypeEnum;
import top.soulblack.rabbit.api.model.Message;
import top.soulblack.rabbit.producer.broker.ProducerClient;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by lxf on 2020/10/20
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageSendTest {

    @Autowired
    private ProducerClient producerClient;

    @Test
    public void testProducerClient() throws Exception {
        for (int i = 0; i < 1; i++) {
            String uniqueId = UUID.randomUUID().toString();
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("name", "张三");
            attributes.put("age", "18");
            Message message = new Message(
                    uniqueId,
                    "exchange-1",
                    "springboot.rabbit",
                    attributes,
                    5000,
                    MessageTypeEnum.RELIANT_MESSAGE.name()
            );

            producerClient.send(message);
        }
        Thread.sleep(50000);
    }
}
