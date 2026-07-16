package com.denizbyrk.sffecommerce.notification_service;

import com.denizbyrk.sffecommerce.notification_service.service.EmailService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void sendWelcomeEmail_shouldSendEmailSuccessfully() {

        String email = "test@gmail.com";
        String username = "deniz";

        this.emailService.sendWelcomeEmail(email, username);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(this.javaMailSender).send(captor.capture());

        SimpleMailMessage message = captor.getValue();

        assertEquals(
                email,
                message.getTo()[0]
        );

        assertEquals(
                "Welcome to SFF E-Commerce",
                message.getSubject()
        );

        assertEquals(
                "Hello deniz,\n\nYour account has been successfully created!",
                message.getText()
        );
    }

    @Test
    void sendOrderConfirmationEmail_shouldSendEmailSuccessfully() {

        String email = "customer@gmail.com";
        Long orderId = 50L;

        this.emailService.sendOrderConfirmationEmail(email, orderId);

        ArgumentCaptor<SimpleMailMessage> captor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(this.javaMailSender).send(captor.capture());

        SimpleMailMessage message = captor.getValue();

        assertEquals(
                email,
                message.getTo()[0]
        );

        assertEquals(
                "Order Confirmation",
                message.getSubject()
        );

        assertEquals(
                "Your order #50 has been created successfully.",
                message.getText()
        );
    }
}