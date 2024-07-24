package org.tsing.learning.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.tsing.learning.utils.UUIDUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RMProducer {
    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        //2.设置host连接
        factory.setHost("localhost");

        //3.建立到代理服务器的连
        Connection conn = factory.newConnection();
        //4.获得信道
        Channel channel = conn.createChannel();

        //5.生命交换器
        String exchangeName = "hello-exchange";
        channel.exchangeDeclare(exchangeName, "direct", true);

        String routingKey = "test.my.routing";

        try {
            Thread.sleep(500);
            String str = UUIDUtils.buildUUId(routingKey);

            //6.发布消息
            channel.basicPublish(exchangeName, routingKey, null, str.getBytes());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        //7.关闭信道和连接
        channel.close();
        conn.close();
    }
}
