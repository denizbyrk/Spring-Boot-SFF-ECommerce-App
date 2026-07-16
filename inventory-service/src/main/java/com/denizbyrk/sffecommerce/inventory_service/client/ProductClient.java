package com.denizbyrk.sffecommerce.inventory_service.client;

import com.denizbyrk.sffecommerce.inventory_service.config.FeignConfig;
import com.denizbyrk.sffecommerce.inventory_service.DTO.ProductResponseDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", configuration = FeignConfig.class)
public interface ProductClient {

    @GetMapping("/api/internal/product/{id}")
    ProductResponseDTO getProductById(@PathVariable Long id);
}