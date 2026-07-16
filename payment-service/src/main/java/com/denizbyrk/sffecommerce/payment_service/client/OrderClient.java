package com.denizbyrk.sffecommerce.payment_service.client;

import com.denizbyrk.sffecommerce.payment_service.DTO.OrderResponseDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service")
public interface OrderClient {

    @GetMapping("/api/internal/order/{id}")
    OrderResponseDTO getOrderById(@PathVariable Long id);

    @PatchMapping("/api/internal/order/{id}/pay")
    void markOrderAsPaid(@PathVariable Long id);
}