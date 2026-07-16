package com.denizbyrk.sffecommerce.order_service.DTO;

import com.denizbyrk.sffecommerce.order_service.entity.OrderStatus;

import com.denizbyrk.sffecommerce.order_service.entity.PaymentStatus;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;

import java.util.List;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

    private Long orderId;
    private Long userId;
    private Double totalPrice;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
    private List<OrderItemResponseDTO> items;
}