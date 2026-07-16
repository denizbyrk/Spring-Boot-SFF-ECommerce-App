package com.denizbyrk.sffecommerce.inventory_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PRODUCT_QUEUE = "product.created.queue";

    @Bean
    public Queue productQueue() {

        return new Queue(PRODUCT_QUEUE);
    }
}