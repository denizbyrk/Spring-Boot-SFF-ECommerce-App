package com.denizbyrk.sffecommerce.product_service.event;

import com.denizbyrk.sffecommerce.product_service.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public ProductEventPublisher(RabbitTemplate rabbitTemplate) {

        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendProductCreatedEvent(Long productId) {

        ProductCreatedEvent event =
                ProductCreatedEvent.builder()
                        .productId(productId)
                        .build();


        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PRODUCT_EXCHANGE,
                RabbitMQConfig.PRODUCT_ROUTING_KEY,
                event
        );
    }
}