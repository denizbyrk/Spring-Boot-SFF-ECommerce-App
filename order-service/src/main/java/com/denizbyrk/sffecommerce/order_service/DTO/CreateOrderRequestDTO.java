package com.denizbyrk.sffecommerce.order_service.DTO;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequestDTO {

    private List<OrderItemRequestDTO> items;
}