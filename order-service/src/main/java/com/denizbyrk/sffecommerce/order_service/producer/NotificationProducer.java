package com.denizbyrk.sffecommerce.order_service.producer;

import com.denizbyrk.sffecommerce.order_service.config.RabbitMQConfig;
import com.denizbyrk.sffecommerce.order_service.event.OrderCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public NotificationProducer(RabbitTemplate rabbitTemplate) {

        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrderCreatedEvent(OrderCreatedEvent event) {

        this.rabbitTemplate.convertAndSend(
                RabbitMQConfig.NOTIFICATION_EXCHANGE,
                RabbitMQConfig.ORDER_CREATED_ROUTING_KEY,
                event
        );
    }
}