package com.denizbyrk.sffecommerce.product_service.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long sellerId;

    @Column(nullable = false)
    private String sellerName;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double price;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus productStatus;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (this.productStatus == null) {

            this.productStatus = ProductStatus.ACTIVE;
        }
    }

    @PreUpdate
    public void onUpdate() {

        this.updatedAt = LocalDateTime.now();
    }
}