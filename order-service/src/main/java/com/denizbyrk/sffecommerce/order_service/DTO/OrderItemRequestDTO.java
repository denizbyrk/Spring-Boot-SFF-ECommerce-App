package com.denizbyrk.sffecommerce.order_service.DTO;

import lombok.Data;

@Data
public class OrderItemRequestDTO {

    private Long productId;
    private Integer quantity;
}