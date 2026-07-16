package com.denizbyrk.sffecommerce.notification_service;

import com.denizbyrk.sffecommerce.notification_service.service.EmailService;
import com.denizbyrk.sffecommerce.notification_service.event.UserCreatedEvent;
import com.denizbyrk.sffecommerce.notification_service.event.OrderCreatedEvent;
import com.denizbyrk.sffecommerce.notification_service.consumer.NotificationConsumer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationConsumerTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificationConsumer notificationConsumer;

    @Test
    void handleUserCreated_shouldSendWelcomeEmail() {

        UserCreatedEvent event =
                new UserCreatedEvent(
                        1L,
                        "deniz",
                        "deniz@gmail.com"
                );

        this.notificationConsumer.handleUserCreated(event);

        verify(this.emailService)
                .sendWelcomeEmail(
                        "deniz@gmail.com",
                        "deniz"
                );
    }

    @Test
    void handleOrderCreated_shouldSendConfirmationEmail() {

        OrderCreatedEvent event =
                new OrderCreatedEvent(
                        100L,
                        "customer@gmail.com",
                        250.0
                );

        this.notificationConsumer.handleOrderCreated(event);

        verify(this.emailService)
                .sendOrderConfirmationEmail(
                        "customer@gmail.com",
                        100L
                );
    }
}