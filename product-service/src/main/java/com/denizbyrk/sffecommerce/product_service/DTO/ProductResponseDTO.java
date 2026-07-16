package com.denizbyrk.sffecommerce.product_service.DTO;

import com.denizbyrk.sffecommerce.product_service.entity.Category;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDTO {

    private Long id;
    private String name;
    private Category category;
    private Double price;
    private String description;
    private String sellerUsername;
}