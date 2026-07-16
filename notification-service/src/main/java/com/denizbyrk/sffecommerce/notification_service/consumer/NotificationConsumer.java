package com.denizbyrk.sffecommerce.notification_service.consumer;

import com.denizbyrk.sffecommerce.notification_service.config.RabbitMQConfig;
import com.denizbyrk.sffecommerce.notification_service.event.OrderCreatedEvent;
import com.denizbyrk.sffecommerce.notification_service.event.UserCreatedEvent;
import com.denizbyrk.sffecommerce.notification_service.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationConsumer {

    private final EmailService emailService;

    public NotificationConsumer(EmailService emailService) {

        this.emailService = emailService;
    }

    @RabbitListener(queues = RabbitMQConfig.USER_CREATED_QUEUE)
    public void handleUserCreated(UserCreatedEvent event) {

        log.info("Received UserCreatedEvent for user {}", event.getUsername());

        this.emailService.sendWelcomeEmail(
                event.getEmail(),
                event.getUsername()
        );
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_CREATED_QUEUE)
    public void handleOrderCreated(OrderCreatedEvent event) {

        log.info("Received OrderCreatedEvent for order {}", event.getOrderId());

        this.emailService.sendOrderConfirmationEmail(
                event.getEmail(),
                event.getOrderId()
        );
    }
}