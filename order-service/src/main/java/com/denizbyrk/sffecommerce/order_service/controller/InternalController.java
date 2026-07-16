package com.denizbyrk.sffecommerce.order_service.controller;

import com.denizbyrk.sffecommerce.order_service.entity.OrderStatus;
import com.denizbyrk.sffecommerce.order_service.DTO.OrderResponseDTO;
import com.denizbyrk.sffecommerce.order_service.service.OrderService;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/api/internal/order")
public class InternalController {

    private final OrderService orderService;

    public InternalController(OrderService orderService) {

        this.orderService = orderService;
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {

        this.orderService.updateStatus(id, status);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public OrderResponseDTO getOrderById(@PathVariable Long id) {

        return this.orderService.getInternalOrderById(id);
    }

    @PatchMapping("/{id}/pay")
    public void markOrderAsPaid(@PathVariable Long id) {

        this.orderService.markOrderAsPaid(id);
    }
}