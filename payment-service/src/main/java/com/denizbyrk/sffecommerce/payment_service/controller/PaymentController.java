package com.denizbyrk.sffecommerce.payment_service.controller;

import com.denizbyrk.sffecommerce.payment_service.DTO.PaymentRequestDTO;
import com.denizbyrk.sffecommerce.payment_service.DTO.PaymentResponseDTO;
import com.denizbyrk.sffecommerce.payment_service.service.PaymentService;
import com.denizbyrk.sffecommerce.payment_service.DTO.PaymentStatusUpdateDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {

        this.paymentService = paymentService;
    }

    ///
    /// Process payment
    ///
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> processPayment(@RequestBody PaymentRequestDTO request) {

        PaymentResponseDTO response = this.paymentService.processPayment(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    ///
    /// Get payment by payment id
    ///
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable Long id) {

        return ResponseEntity.ok(this.paymentService.getPaymentById(id));
    }

    ///
    /// Get payment by order id
    ///
    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByOrderId(@PathVariable Long orderId) {

        return ResponseEntity.ok(this.paymentService.getPaymentByOrderId(orderId));
    }

    ///
    /// Get logged-in user's payments
    ///
    @GetMapping("/my-payments")
    public ResponseEntity<List<PaymentResponseDTO>> getMyPayments() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(this.paymentService.getPaymentsOfCurrentUser(authentication.getName()));
    }

    ///
    /// Update payment status
    ///
    @PatchMapping("/{id}/status")
    public ResponseEntity<PaymentResponseDTO> updateStatus(@PathVariable Long id, @RequestBody PaymentStatusUpdateDTO dto) {

        return ResponseEntity.ok(this.paymentService.updateStatus(id, dto));
    }
}