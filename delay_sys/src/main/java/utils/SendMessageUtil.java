package utils;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

public class SendMessageUtil {

    public static DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");

    static {
        producer.setNamesrvAddr("127.0.0.1:9876;");
        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public static SendResult sendMessage(Message message) {
//        Message msg = new Message("TopicTest" /* Topic */,
//                "TagA" /* Tag */,
//                ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
//        );
//                msg.setTags();

        SendResult sendResult = null;
        try {
            sendResult = producer.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sendResult;
    }
}
