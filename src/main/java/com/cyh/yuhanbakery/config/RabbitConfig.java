package com.cyh.yuhanbakery.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    public static final String TTL_QUEUE = "order.ttl.queue";
    public static final String CANCEL_EXCHANGE = "order.cancel.exchange";
    public static final String CANCEL_QUEUE = "order.delay.queue";
    public static final String ROUTING_KEY = "order.cancel";

    /**
     * 声明 TTL 延迟存储队列 (不设消费者，消息在此处静置 15 分钟，过期后进入死信交换机)
     */
    @Bean
    public Queue ttlQueue() {
        Map<String, Object> args = new HashMap<>();
        // 15分钟超时：15 * 60 * 1000 = 900000ms
        args.put("x-message-ttl", 900000);
        // 配置死信接收目标交换机
        args.put("x-dead-letter-exchange", CANCEL_EXCHANGE);
        // 配置死信投递路由键
        args.put("x-dead-letter-routing-key", ROUTING_KEY);
        return QueueBuilder.durable(TTL_QUEUE).withArguments(args).build();
    }

    /**
     * 声明死信交换机 (普通 Direct 交换机即可，无需任何插件)
     */
    @Bean
    public DirectExchange cancelExchange() {
        return new DirectExchange(CANCEL_EXCHANGE, true, false);
    }

    /**
     * 声明实际消费队列 (即原有的 order.delay.queue)
     */
    @Bean
    public Queue cancelQueue() {
        return QueueBuilder.durable(CANCEL_QUEUE).build();
    }

    /**
     * 绑定消费队列到死信交换机
     */
    @Bean
    public Binding cancelBinding(Queue cancelQueue, DirectExchange cancelExchange) {
        return BindingBuilder.bind(cancelQueue).to(cancelExchange).with(ROUTING_KEY);
    }
}
