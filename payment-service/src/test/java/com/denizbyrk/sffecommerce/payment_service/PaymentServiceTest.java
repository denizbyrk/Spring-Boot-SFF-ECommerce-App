package com.denizbyrk.sffecommerce.payment_service;

import com.denizbyrk.sffecommerce.payment_service.entity.Payment;
import com.denizbyrk.sffecommerce.payment_service.client.UserClient;
import com.denizbyrk.sffecommerce.payment_service.client.OrderClient;
import com.denizbyrk.sffecommerce.payment_service.entity.OrderStatus;
import com.denizbyrk.sffecommerce.payment_service.DTO.UserResponseDTO;
import com.denizbyrk.sffecommerce.payment_service.entity.PaymentMethod;
import com.denizbyrk.sffecommerce.payment_service.entity.PaymentStatus;
import com.denizbyrk.sffecommerce.payment_service.DTO.OrderResponseDTO;
import com.denizbyrk.sffecommerce.payment_service.DTO.PaymentRequestDTO;
import com.denizbyrk.sffecommerce.payment_service.DTO.PaymentResponseDTO;
import com.denizbyrk.sffecommerce.payment_service.service.PaymentService;
import com.denizbyrk.sffecommerce.payment_service.DTO.PaymentStatusUpdateDTO;
import com.denizbyrk.sffecommerce.payment_service.event.PaymentCompletedEvent;
import com.denizbyrk.sffecommerce.payment_service.repository.PaymentRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.Authentication;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private UserClient userClient;

    @Mock
    private OrderClient orderClient;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PaymentService paymentService;

    private UserResponseDTO user;
    private OrderResponseDTO order;
    private PaymentRequestDTO request;

    @BeforeEach
    void setup() {

        this. user = new UserResponseDTO();
        this.user.setId(1L);
        this.user.setUsername("deniz");

        this.order = OrderResponseDTO.builder()
                .orderId(10L)
                .userId(1L)
                .totalPrice(150.0)
                .status(OrderStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        this.request = PaymentRequestDTO.builder()
                .orderId(10L)
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .cardHolderName("Deniz")
                .cardNumber("123456789")
                .expiryDate("12/30")
                .cvv("123")
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "deniz",
                        null
                );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
    }

    @AfterEach
    void cleanup() {

        SecurityContextHolder.clearContext();
    }

    @Test
    void processPayment_shouldCreatePaymentSuccessfully() {

        when(this.userClient.getUserByUsername("deniz")).thenReturn(this.user);

        when(this.orderClient.getOrderById(10L)).thenReturn(this.order);

        Payment savedPayment = Payment.builder()
                .id(100L)
                .orderId(10L)
                .userId(1L)
                .amount(150.0)
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .status(PaymentStatus.PAID)
                .transactionId("abc-123")
                .build();

        when(this.paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        PaymentResponseDTO response = this.paymentService.processPayment(this.request);

        assertNotNull(response);

        assertEquals(100L, response.getId());
        assertEquals(10L, response.getOrderId());
        assertEquals(150.0, response.getAmount());
        assertEquals(PaymentStatus.PAID, response.getStatus());

        verify(this.paymentRepository).save(any(Payment.class));

        verify(this.rabbitTemplate)
                .convertAndSend(
                        anyString(),
                        anyString(),
                        any(PaymentCompletedEvent.class)
                );
    }


    @Test
    void processPayment_shouldThrowException_whenUserDoesNotOwnOrder() {

        this.order.setUserId(99L);

        when(this.userClient.getUserByUsername("deniz")).thenReturn(this.user);

        when(this.orderClient.getOrderById(10L)).thenReturn(this.order);

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> this.paymentService.processPayment(this.request)
                );

        assertEquals(
                "You are not allowed to pay for this order.",
                exception.getMessage()
        );

        verify(this.paymentRepository, never()).save(any());
    }

    @Test
    void processPayment_shouldThrowException_whenOrderAlreadyPaid() {

        this.order.setPaymentStatus(PaymentStatus.PAID);


        when(this.userClient.getUserByUsername("deniz")).thenReturn(this.user);

        when(this.orderClient.getOrderById(10L)).thenReturn(this.order);

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> this.paymentService.processPayment(this.request)
                );

        assertEquals(
                "This order has already been paid.",
                exception.getMessage()
        );

        verify(this.paymentRepository, never()).save(any());
    }

    @Test
    void getPaymentById_shouldReturnPayment() {

        Payment payment = Payment.builder()
                .id(1L)
                .orderId(10L)
                .userId(1L)
                .amount(100.0)
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .status(PaymentStatus.PAID)
                .transactionId("tx123")
                .build();

        when(this.paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        PaymentResponseDTO response = this.paymentService.getPaymentById(1L);

        assertEquals(1L, response.getId());
        assertEquals(100.0, response.getAmount());
        assertEquals(PaymentStatus.PAID, response.getStatus());
    }

    @Test
    void getPaymentById_shouldThrowException_whenPaymentNotFound() {

        when(this.paymentRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> this.paymentService.getPaymentById(1L)
                );

        assertEquals(
                "Payment not found.",
                exception.getMessage()
        );
    }

    @Test
    void updateStatus_shouldChangePaymentStatus() {

        Payment payment = Payment.builder()
                .id(1L)
                .status(PaymentStatus.PENDING)
                .build();

        when(this.paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        when(this.paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentStatusUpdateDTO dto =
                new PaymentStatusUpdateDTO(
                        PaymentStatus.REFUNDED
                );

        PaymentResponseDTO response = this.paymentService.updateStatus(1L, dto);

        assertEquals(
                PaymentStatus.REFUNDED,
                response.getStatus()
        );

        verify(this.paymentRepository).save(payment);
    }
}