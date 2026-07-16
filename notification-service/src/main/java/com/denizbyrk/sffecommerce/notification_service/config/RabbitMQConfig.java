package com.denizbyrk.sffecommerce.notification_service.config;

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

    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";

    public static final String USER_CREATED_QUEUE = "user.created.queue";
    public static final String ORDER_CREATED_QUEUE = "order.created.queue";

    public static final String USER_CREATED_ROUTING_KEY = "user.created";
    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";

    @Bean
    public TopicExchange notificationExchange() {

        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Queue userCreatedQueue() {

        return new Queue(USER_CREATED_QUEUE);
    }

    @Bean
    public Queue orderCreatedQueue() {

        return new Queue(ORDER_CREATED_QUEUE);
    }

    @Bean
    public Binding userCreatedBinding() {
        return BindingBuilder
                .bind(userCreatedQueue())
                .to(notificationExchange())
                .with(USER_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding orderCreatedBinding() {
        return BindingBuilder
                .bind(orderCreatedQueue())
                .to(notificationExchange())
                .with(ORDER_CREATED_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {

        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {

        RabbitTemplate template = new RabbitTemplate(connectionFactory);

        template.setMessageConverter(converter);

        return template;
    }
}