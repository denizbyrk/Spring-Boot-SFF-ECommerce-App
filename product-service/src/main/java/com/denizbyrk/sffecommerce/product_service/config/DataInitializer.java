package com.denizbyrk.sffecommerce.product_service.config;

import com.denizbyrk.sffecommerce.product_service.entity.Product;
import com.denizbyrk.sffecommerce.product_service.entity.Category;
import com.denizbyrk.sffecommerce.product_service.entity.ProductStatus;
import com.denizbyrk.sffecommerce.product_service.repository.ProductRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initProducts(ProductRepository productRepository) {

        return args -> {

            if (productRepository.count() > 0) {
                return;
            }

            productRepository.save(Product.builder()
                    .name("Gaming Computer")
                    .category(Category.Electronics)
                    .description("Gaming PC with RTX graphics")
                    .price(1500.0)
                    .sellerId(1L)
                    .sellerName("admin")
                    .imageUrl("")
                    .productStatus(ProductStatus.ACTIVE)
                    .build());

            productRepository.save(Product.builder()
                    .name("3x3 Rubik's Cube")
                    .category(Category.Toys)
                    .description("Classic speed cube")
                    .price(10.0)
                    .sellerId(2L)
                    .sellerName("test")
                    .imageUrl("")
                    .productStatus(ProductStatus.ACTIVE)
                    .build());

            productRepository.save(Product.builder()
                    .name("Basic T-Shirt")
                    .category(Category.Clothing)
                    .description("Soft cotton t-shirt")
                    .price(50.0)
                    .sellerId(2L)
                    .sellerName("test")
                    .imageUrl("")
                    .productStatus(ProductStatus.ACTIVE)
                    .build());

            productRepository.save(Product.builder()
                    .name("Wireless Mouse")
                    .category(Category.Electronics)
                    .description("Ergonomic wireless mouse")
                    .price(35.0)
                    .sellerId(1L)
                    .sellerName("admin")
                    .imageUrl("")
                    .productStatus(ProductStatus.ACTIVE)
                    .build());

            productRepository.save(Product.builder()
                    .name("Mechanical Keyboard")
                    .category(Category.Electronics)
                    .description("RGB mechanical keyboard")
                    .price(120.0)
                    .sellerId(1L)
                    .sellerName("admin")
                    .imageUrl("")
                    .productStatus(ProductStatus.ACTIVE)
                    .build());

            productRepository.save(Product.builder()
                    .name("Java Programming")
                    .category(Category.Books)
                    .description("Comprehensive Java guide")
                    .price(45.0)
                    .sellerId(2L)
                    .sellerName("test")
                    .imageUrl("")
                    .productStatus(ProductStatus.ACTIVE)
                    .build());

            productRepository.save(Product.builder()
                    .name("Office Chair")
                    .category(Category.Furniture)
                    .description("Comfortable ergonomic chair")
                    .price(220.0)
                    .sellerId(1L)
                    .sellerName("admin")
                    .imageUrl("")
                    .productStatus(ProductStatus.ACTIVE)
                    .build());

            productRepository.save(Product.builder()
                    .name("Basketball")
                    .category(Category.Sports)
                    .description("Official size basketball")
                    .price(35.0)
                    .sellerId(2L)
                    .sellerName("test")
                    .imageUrl("")
                    .productStatus(ProductStatus.ACTIVE)
                    .build());

            productRepository.save(Product.builder()
                    .name("Football")
                    .category(Category.Sports)
                    .description("Professional football")
                    .price(30.0)
                    .sellerId(1L)
                    .sellerName("admin")
                    .imageUrl("")
                    .productStatus(ProductStatus.ACTIVE)
                    .build());

            productRepository.save(Product.builder()
                    .name("Face Cream")
                    .category(Category.Cosmetic)
                    .description("Daily moisturizing cream")
                    .price(18.0)
                    .sellerId(2L)
                    .sellerName("test")
                    .imageUrl("")
                    .productStatus(ProductStatus.ACTIVE)
                    .build());

            productRepository.save(Product.builder()
                    .name("Lipstick")
                    .category(Category.Cosmetic)
                    .description("Long-lasting matte lipstick")
                    .price(20.0)
                    .sellerId(1L)
                    .sellerName("admin")
                    .imageUrl("")
                    .productStatus(ProductStatus.ACTIVE)
                    .build());

            productRepository.save(Product.builder()
                    .name("Bookshelf")
                    .category(Category.Furniture)
                    .description("Wooden 5-level bookshelf")
                    .price(180.0)
                    .sellerId(2L)
                    .sellerName("test")
                    .imageUrl("")
                    .productStatus(ProductStatus.ACTIVE)
                    .build());

            productRepository.save(Product.builder()
                    .name("Coffee Table")
                    .category(Category.Furniture)
                    .description("Modern living room table")
                    .price(140.0)
                    .sellerId(1L)
                    .sellerName("admin")
                    .imageUrl("")
                    .productStatus(ProductStatus.ACTIVE)
                    .build());

            productRepository.save(Product.builder()
                    .name("Monitor")
                    .category(Category.Electronics)
                    .description("27-inch 144Hz monitor")
                    .price(350.0)
                    .sellerId(2L)
                    .sellerName("test")
                    .imageUrl("")
                    .productStatus(ProductStatus.ACTIVE)
                    .build());

            productRepository.save(Product.builder()
                    .name("Bluetooth Speaker")
                    .category(Category.Electronics)
                    .description("Portable waterproof speaker")
                    .price(70.0)
                    .sellerId(1L)
                    .sellerName("admin")
                    .imageUrl("")
                    .productStatus(ProductStatus.ACTIVE)
                    .build());

            productRepository.save(Product.builder()
                    .name("Running Shoes")
                    .category(Category.Clothing)
                    .description("Lightweight running shoes")
                    .price(95.0)
                    .sellerId(2L)
                    .sellerName("test")
                    .imageUrl("")
                    .productStatus(ProductStatus.ACTIVE)
                    .build());

            productRepository.save(Product.builder()
                    .name("Hoodie")
                    .category(Category.Clothing)
                    .description("Warm fleece hoodie")
                    .price(80.0)
                    .sellerId(1L)
                    .sellerName("admin")
                    .imageUrl("")
                    .productStatus(ProductStatus.ACTIVE)
                    .build());

            productRepository.save(Product.builder()
                    .name("Chess Set")
                    .category(Category.Toys)
                    .description("Wooden chess set")
                    .price(60.0)
                    .sellerId(2L)
                    .sellerName("test")
                    .imageUrl("")
                    .productStatus(ProductStatus.ACTIVE)
                    .build());

            productRepository.save(Product.builder()
                    .name("LEGO Sports Car")
                    .category(Category.Toys)
                    .description("Building set with 900 pieces")
                    .price(90.0)
                    .sellerId(1L)
                    .sellerName("admin")
                    .imageUrl("")
                    .productStatus(ProductStatus.ACTIVE)
                    .build());

            productRepository.save(Product.builder()
                    .name("Clean Code")
                    .category(Category.Books)
                    .description("A handbook of agile software craftsmanship")
                    .price(55.0)
                    .sellerId(2L)
                    .sellerName("test")
                    .imageUrl("")
                    .productStatus(ProductStatus.ACTIVE)
                    .build());
        };
    }
}