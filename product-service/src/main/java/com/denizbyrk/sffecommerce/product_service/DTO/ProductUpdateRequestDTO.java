package com.denizbyrk.sffecommerce.product_service.DTO;

import com.denizbyrk.sffecommerce.product_service.entity.Category;

import lombok.Data;

@Data
public class ProductUpdateRequestDTO {

    private Long id;
    private String name;
    private Category category;
    private Double price;
    private String description;
    private String sellerUsername;
}