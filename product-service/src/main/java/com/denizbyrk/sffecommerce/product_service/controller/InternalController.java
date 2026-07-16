package com.denizbyrk.sffecommerce.product_service.controller;

import com.denizbyrk.sffecommerce.product_service.DTO.ProductResponseDTO;
import com.denizbyrk.sffecommerce.product_service.service.ProductService;

import io.swagger.v3.oas.annotations.Hidden;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/api/internal/product")
public class InternalController {

    private final ProductService productService;

    public InternalController(ProductService productService) {

        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ProductResponseDTO getProduct(@PathVariable Long id) {

        return this.productService.getProduct(id);
    }
}