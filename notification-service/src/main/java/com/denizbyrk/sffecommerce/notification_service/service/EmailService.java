package com.denizbyrk.sffecommerce.notification_service.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {

        this.javaMailSender = javaMailSender;
    }

    public void sendWelcomeEmail(String email, String username) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Welcome to SFF E-Commerce");

        message.setText("Hello " + username + ",\n\nYour account has been successfully created!");

        this.javaMailSender.send(message);
    }

    public void sendOrderConfirmationEmail(String email, Long orderId) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Order Confirmation");

        message.setText("Your order #" + orderId + " has been created successfully.");

        this.javaMailSender.send(message);
    }
}