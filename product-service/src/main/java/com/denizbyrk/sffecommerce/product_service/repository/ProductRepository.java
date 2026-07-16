package com.denizbyrk.sffecommerce.product_service.repository;

import com.denizbyrk.sffecommerce.product_service.entity.Product;
import com.denizbyrk.sffecommerce.product_service.entity.Category;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByName(String name);

    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findByCategory(Category category);

    List<Product> findBySellerId(Long id);

    List<Product> findBySellerName(String username);
}