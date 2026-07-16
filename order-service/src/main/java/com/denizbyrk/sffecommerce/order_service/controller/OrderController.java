package com.denizbyrk.sffecommerce.order_service.controller;

import com.denizbyrk.sffecommerce.order_service.service.OrderService;
import com.denizbyrk.sffecommerce.order_service.DTO.OrderResponseDTO;
import com.denizbyrk.sffecommerce.order_service.DTO.CreateOrderRequestDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {

        this.orderService = orderService;
    }

    ///
    /// create order
    ///
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody CreateOrderRequestDTO request) {

        OrderResponseDTO response = this.orderService.createOrder(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    ///
    /// get logged in user's orders
    ///
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders() {

        return ResponseEntity.ok(this.orderService.getMyOrders());
    }

    ///
    /// get order by id
    ///
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {

        return ResponseEntity.ok(this.orderService.getInternalOrderById(id));
    }

    ///
    /// cancel order
    ///
    @DeleteMapping("/delete-order/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {

        this.orderService.cancelOrder(id);

        return ResponseEntity.noContent().build();
    }

    ///
    /// admin get all orders
    ///
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {

        return ResponseEntity.ok(this.orderService.getAllOrders());
    }
}