package com.denizbyrk.sffecommerce.order_service.DTO;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class OrderItemResponseDTO {

    private Long productId;
    private String productName;
    private Double unitPrice;
    private Integer quantity;
    private Double subtotal;
}