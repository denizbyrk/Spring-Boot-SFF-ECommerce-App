package com.denizbyrk.sffecommerce.order_service.client;

import com.denizbyrk.sffecommerce.order_service.config.FeignConfig;
import com.denizbyrk.sffecommerce.order_service.DTO.ProductResponseDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", configuration = FeignConfig.class)
public interface ProductClient {

    @GetMapping("/api/internal/product/{id}")
    ProductResponseDTO getProductById(@PathVariable Long id);
}