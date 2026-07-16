package com.denizbyrk.sffecommerce.payment_service.repository;

import com.denizbyrk.sffecommerce.payment_service.entity.Payment;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTransactionId(String transactionId);

    List<Payment> findByUserId(Long userId);

    Optional<Payment> findByOrderId(Long orderId);
}