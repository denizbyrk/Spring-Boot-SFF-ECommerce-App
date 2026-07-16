package com.denizbyrk.sffecommerce.inventory_service.repository;

import com.denizbyrk.sffecommerce.inventory_service.entity.Inventory;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductId(Long productId);

    boolean existsByProductId(Long productId);
}