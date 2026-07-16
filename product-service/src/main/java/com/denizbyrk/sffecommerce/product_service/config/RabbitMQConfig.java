package com.denizbyrk.sffecommerce.product_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PRODUCT_EXCHANGE = "product.exchange";
    public static final String PRODUCT_QUEUE = "product.created.queue";
    public static final String PRODUCT_ROUTING_KEY = "product.created";

    @Bean
    public TopicExchange productExchange() {

        return new TopicExchange(PRODUCT_EXCHANGE);
    }

    @Bean
    public Queue productQueue() {

        return new Queue(PRODUCT_QUEUE);
    }

    @Bean
    public Binding productBinding(Queue productQueue, TopicExchange productExchange) {

        return BindingBuilder
                .bind(productQueue)
                .to(productExchange)
                .with(PRODUCT_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {

        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {

        RabbitTemplate template = new RabbitTemplate(connectionFactory);

        template.setMessageConverter(converter);

        return template;
    }
}