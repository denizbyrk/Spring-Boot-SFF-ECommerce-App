package com.denizbyrk.sffecommerce.payment_service.DTO;

import com.denizbyrk.sffecommerce.payment_service.entity.OrderStatus;
import com.denizbyrk.sffecommerce.payment_service.entity.PaymentStatus;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

    private Long orderId;
    private Long userId;
    private Double totalPrice;
    private OrderStatus status;
    private PaymentStatus paymentStatus;
}