package com.cyh.yuhanbakery.mq;

import com.rabbitmq.client.Channel;
import com.cyh.yuhanbakery.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCancelListener {

    private final OrderService orderService;

    @RabbitListener(queues = "order.delay.queue")
    public void processOrderCancel(String orderNo, Message message, Channel channel) throws IOException {
        log.info("【RabbitMQ】收到订单超时未支付自动取消消息，orderNo={}", orderNo);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            orderService.cancelOrder(orderNo);
            // 手动确认 ACK，告知 RabbitMQ 消息已被顺利消费
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("处理订单超时取消发生异常，将重新放入队列进行补偿：", e);
            // 处理失败，第三个参数为 true 代表重新塞回队列
            channel.basicNack(deliveryTag, false, true);
        }
    }
}
