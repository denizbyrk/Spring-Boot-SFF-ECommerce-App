package com.denizbyrk.sffecommerce.payment_service.DTO;

import com.denizbyrk.sffecommerce.payment_service.entity.PaymentMethod;
import com.denizbyrk.sffecommerce.payment_service.entity.PaymentStatus;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {

    private Long id;
    private Long orderId;
    private Double amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String transactionId;
    private LocalDateTime createdAt;
}