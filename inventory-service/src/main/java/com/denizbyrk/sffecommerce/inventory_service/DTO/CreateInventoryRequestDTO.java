package com.denizbyrk.sffecommerce.inventory_service.DTO;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateInventoryRequestDTO {

    private Long productId;

    private Integer quantity;
}
