package com.oilerrig.backend.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String ORDER_SAGA_QUEUE = "order.saga.queue";
    public static final String ORDER_SAGA_EXCHANGE = "order.saga.exchange";
    public static final String ORDER_SAGA_ROUTING_KEY = "order.saga.key";

    @Bean
    public Queue orderSagaQueue() {
        return new Queue(ORDER_SAGA_QUEUE, true);
    }

    @Bean
    public TopicExchange orderSagaExchange() {
        return new TopicExchange(ORDER_SAGA_EXCHANGE);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(orderSagaQueue())
                .to(orderSagaExchange())
                .with(ORDER_SAGA_ROUTING_KEY);
    }
}
