package org.tsing.learning.rabbitmq;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
public class RMConsumer2 {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");

        factory.setHost("localhost");

        Connection conn = factory.newConnection();

        Channel channel = conn.createChannel();

        String exchangeName = "hello-exchange";
        channel.exchangeDeclare(exchangeName, "direct", true);

        //获取队列
        String queueName = channel.queueDeclare().getQueue();
        String rootingKey = "test.my.routing";
        //通过键（routingKey）将队列和交换器绑定
        channel.exchangeBind(queueName, exchangeName, rootingKey);
        while (true) {
            boolean autoAck = false;
            String consumerTag = "";

            channel.basicConsume(queueName, autoAck, consumerTag, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String routKey = envelope.getRoutingKey();
                    String contentType = properties.getContentType();
                    long deliverTag = envelope.getDeliveryTag();

                    //确认消息
                    channel.basicAck(deliverTag, false);

                    log.info("routingkey:{},contentType:{},message:{}", routKey, contentType, new String(body));
                }
            });
        }

    }
}
