package com.denizbyrk.sffecommerce.product_service.DTO;

import com.denizbyrk.sffecommerce.product_service.entity.Category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ProductRequestDTO {

    @NotBlank
    private String name;

    @NotBlank
    private Category category;

    @NotBlank
    private String description;

    @NotNull
    private Double price;

    private String imageUrl;
}