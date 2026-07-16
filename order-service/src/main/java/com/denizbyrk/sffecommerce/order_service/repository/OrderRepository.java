package com.denizbyrk.sffecommerce.order_service.repository;

import com.denizbyrk.sffecommerce.order_service.entity.Order;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);
}