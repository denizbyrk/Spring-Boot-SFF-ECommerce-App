package com.denizbyrk.sffecommerce.product_service;

import com.denizbyrk.sffecommerce.product_service.entity.Product;
import com.denizbyrk.sffecommerce.product_service.entity.Category;
import com.denizbyrk.sffecommerce.product_service.client.UserClient;
import com.denizbyrk.sffecommerce.product_service.DTO.UserResponseDTO;
import com.denizbyrk.sffecommerce.product_service.entity.ProductStatus;
import com.denizbyrk.sffecommerce.product_service.DTO.ProductRequestDTO;
import com.denizbyrk.sffecommerce.product_service.DTO.ProductResponseDTO;
import com.denizbyrk.sffecommerce.product_service.service.ProductService;
import com.denizbyrk.sffecommerce.product_service.event.ProductEventPublisher;
import com.denizbyrk.sffecommerce.product_service.repository.ProductRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserClient userClient;

    @Mock
    private ProductEventPublisher productEventPublisher;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setupSecurityContext() {

        SecurityContextHolder.getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                "deniz",
                                null
                        )
                );
    }

    @Test
    void createProduct_shouldCreateProductSuccessfully() {

        ProductRequestDTO request = new ProductRequestDTO();
        request.setName("Laptop");
        request.setCategory(Category.Electronics);
        request.setDescription("Gaming laptop");
        request.setPrice(1500.0);

        UserResponseDTO user = new UserResponseDTO();

        user.setId(1L);
        user.setUsername("deniz");

        when(this.userClient.getUserByUsername("deniz")).thenReturn(user);

        Product savedProduct = Product.builder()
                .id(10L)
                .sellerId(1L)
                .sellerName("deniz")
                .name("Laptop")
                .category(Category.Electronics)
                .description("Gaming laptop")
                .price(1500.0)
                .productStatus(ProductStatus.ACTIVE)
                .build();


        when(this.productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductResponseDTO response = this.productService.createProduct(request);

        assertEquals(10L, response.getId());
        assertEquals("Laptop", response.getName());
        assertEquals("deniz", response.getSellerUsername());

        verify(productRepository).save(any(Product.class));

        verify(productEventPublisher).sendProductCreatedEvent(10L);
    }

    @Test
    void getProduct_shouldReturnProduct() {

        Product product = Product.builder()
                .id(5L)
                .name("Phone")
                .sellerName("deniz")
                .category(Category.Electronics)
                .price(900.0)
                .description("Smartphone")
                .build();

        when(this.productRepository.findById(5L)).thenReturn(Optional.of(product));

        ProductResponseDTO response = this.productService.getProduct(5L);

        assertEquals(5L, response.getId());
        assertEquals("Phone", response.getName());

        verify(this.productRepository).findById(5L);
    }

    @Test
    void getProduct_shouldThrowExceptionWhenProductNotFound() {

        when(this.productRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> this.productService.getProduct(99L)
        );
    }

    @Test
    void removeProduct_shouldDeactivateProduct() {

        Product product = Product.builder()
                .id(1L)
                .sellerId(1L)
                .productStatus(ProductStatus.ACTIVE)
                .build();

        UserResponseDTO user = new UserResponseDTO();

        user.setId(1L);

        when(this.productRepository.findById(1L)).thenReturn(Optional.of(product));

        when(this.userClient.getUserByUsername("deniz")).thenReturn(user);

        String response = this.productService.removeProduct(1L);

        assertEquals(
                ProductStatus.INACTIVE,
                product.getProductStatus()
        );

        verify(this.productRepository).save(product);

        assertEquals(
                "Product is no longer available.",
                response
        );
    }

    @Test
    void removeProduct_shouldThrowAccessDeniedWhenUserIsNotOwner() {

        Product product = Product.builder()
                .id(1L)
                .sellerId(2L)
                .build();

        UserResponseDTO user = new UserResponseDTO();
        user.setId(1L);

        when(this.productRepository.findById(1L)).thenReturn(Optional.of(product));

        when(this.userClient.getUserByUsername("deniz")).thenReturn(user);

        assertThrows(
                AccessDeniedException.class,
                () -> productService.removeProduct(1L)
        );

        verify(this.productRepository, never()).save(any());
    }
}