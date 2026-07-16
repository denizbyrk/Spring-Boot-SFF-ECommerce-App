package com.denizbyrk.sffecommerce.payment_service.event;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCompletedEvent {

    private Long paymentId;
    private Long orderId;
    private Long userId;
    private Double amount;
    private LocalDateTime createdAt;
}