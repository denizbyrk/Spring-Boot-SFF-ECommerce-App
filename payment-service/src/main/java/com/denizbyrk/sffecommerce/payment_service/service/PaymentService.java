package com.denizbyrk.sffecommerce.payment_service.service;

import com.denizbyrk.sffecommerce.payment_service.entity.Payment;
import com.denizbyrk.sffecommerce.payment_service.client.UserClient;
import com.denizbyrk.sffecommerce.payment_service.client.OrderClient;
import com.denizbyrk.sffecommerce.payment_service.DTO.UserResponseDTO;
import com.denizbyrk.sffecommerce.payment_service.DTO.OrderResponseDTO;
import com.denizbyrk.sffecommerce.payment_service.entity.PaymentStatus;
import com.denizbyrk.sffecommerce.payment_service.config.RabbitMQConfig;
import com.denizbyrk.sffecommerce.payment_service.DTO.PaymentRequestDTO;
import com.denizbyrk.sffecommerce.payment_service.DTO.PaymentResponseDTO;
import com.denizbyrk.sffecommerce.payment_service.DTO.PaymentStatusUpdateDTO;
import com.denizbyrk.sffecommerce.payment_service.event.PaymentCompletedEvent;
import com.denizbyrk.sffecommerce.payment_service.repository.PaymentRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserClient userClient;
    private final OrderClient orderClient;
    private final RabbitTemplate rabbitTemplate;


    public PaymentService(PaymentRepository paymentRepository, UserClient userClient,
                          OrderClient orderClient, RabbitTemplate rabbitTemplate) {

        this.paymentRepository = paymentRepository;
        this.userClient = userClient;
        this.orderClient = orderClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    ///
    /// process payment
    ///
    public PaymentResponseDTO processPayment(PaymentRequestDTO request) {

        Long userId = this.getCurrentUserId();

        OrderResponseDTO order = this.orderClient.getOrderById(request.getOrderId());

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("You are not allowed to pay for this order.");
        }

        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            throw new RuntimeException("This order has already been paid.");
        }

        Payment payment = Payment.builder()
                .orderId(order.getOrderId())
                .userId(userId)
                .amount(order.getTotalPrice())
                .paymentMethod(request.getPaymentMethod())
                .status(PaymentStatus.PAID)
                .transactionId(UUID.randomUUID().toString())
                .build();

        Payment saved = this.paymentRepository.save(payment);

        PaymentCompletedEvent event =
                new PaymentCompletedEvent(
                        saved.getId(),
                        saved.getOrderId(),
                        saved.getUserId(),
                        saved.getAmount(),
                        saved.getCreatedAt()
                );

        log.info(
                "Publishing PaymentCompletedEvent: paymentId={}, orderId={}",
                event.getPaymentId(),
                event.getOrderId()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PAYMENT_EXCHANGE,
                RabbitMQConfig.PAYMENT_ROUTING_KEY,
                event
        );


        return mapToDTO(saved);
    }

    ///
    /// get payment by id
    ///
    public PaymentResponseDTO getPaymentById(Long id) {

        Payment payment = this.paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found."));

        return mapToDTO(payment);
    }

    ///
    /// get by order id
    ///
    public PaymentResponseDTO getPaymentByOrderId(Long orderId) {

        Payment payment = this.paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found."));

        return mapToDTO(payment);
    }

    ///
    /// get logged in user payments
    ///
    public List<PaymentResponseDTO> getPaymentsOfCurrentUser(String username) {

        UserResponseDTO user = this.userClient.getUserByUsername(username);

        return this.paymentRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    ///
    /// update payment status
    ///
    public PaymentResponseDTO updateStatus(Long id, PaymentStatusUpdateDTO dto) {

        Payment payment = this.paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found."));

        payment.setStatus(dto.getStatus());

        Payment updated = this.paymentRepository.save(payment);

        return mapToDTO(updated);
    }

    private Long getCurrentUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserResponseDTO user = this.userClient.getUserByUsername(username);
        return user.getId();
    }

    private PaymentResponseDTO mapToDTO(Payment payment) {

        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .transactionId(payment.getTransactionId())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}