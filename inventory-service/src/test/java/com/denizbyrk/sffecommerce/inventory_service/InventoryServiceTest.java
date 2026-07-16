package com.denizbyrk.sffecommerce.inventory_service;

import com.denizbyrk.sffecommerce.inventory_service.entity.Inventory;
import com.denizbyrk.sffecommerce.inventory_service.client.ProductClient;
import com.denizbyrk.sffecommerce.inventory_service.DTO.ProductResponseDTO;
import com.denizbyrk.sffecommerce.inventory_service.DTO.InventoryResponseDTO;
import com.denizbyrk.sffecommerce.inventory_service.service.InventoryService;
import com.denizbyrk.sffecommerce.inventory_service.DTO.CreateInventoryRequestDTO;
import com.denizbyrk.sffecommerce.inventory_service.DTO.UpdateInventoryRequestDTO;
import com.denizbyrk.sffecommerce.inventory_service.repository.InventoryRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private InventoryService inventoryService;

    private Inventory inventory;

    @BeforeEach
    void setup() {

        this.inventory = Inventory.builder()
                .id(1L)
                .productId(100L)
                .quantity(20)
                .build();
    }

    @Test
    void createInventory_shouldCreateSuccessfully() {

        CreateInventoryRequestDTO request =
                CreateInventoryRequestDTO.builder()
                        .productId(100L)
                        .quantity(20)
                        .build();

        when(this.productClient.getProductById(100L))
                .thenReturn(
                        new ProductResponseDTO(100L, "Laptop")
                );

        when(this.inventoryRepository.existsByProductId(100L)).thenReturn(false);

        when(this.inventoryRepository.save(any(Inventory.class))).thenReturn(this.inventory);

        InventoryResponseDTO response = this.inventoryService.createInventory(request);

        assertEquals(100L, response.getProductId());
        assertEquals(20, response.getQuantity());

        verify(this.inventoryRepository).save(any(Inventory.class));
    }

    @Test
    void createInventory_shouldThrowException_whenInventoryExists() {

        CreateInventoryRequestDTO request =
                CreateInventoryRequestDTO.builder()
                        .productId(100L)
                        .quantity(20)
                        .build();

        when(this.productClient.getProductById(100L))
                .thenReturn(
                        new ProductResponseDTO(100L, "Laptop")
                );

        when(this.inventoryRepository.existsByProductId(100L)).thenReturn(true);

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> this.inventoryService.createInventory(request)
                );

        assertEquals(
                "Inventory already exists for this product",
                exception.getMessage()
        );

        verify(this.inventoryRepository, never()).save(any());
    }

    @Test
    void createInventory_shouldThrowException_whenQuantityNegative() {

        CreateInventoryRequestDTO request =
                CreateInventoryRequestDTO.builder()
                        .productId(100L)
                        .quantity(-5)
                        .build();

        when(this.productClient.getProductById(100L))
                .thenReturn(
                        new ProductResponseDTO(100L, "Laptop")
                );

        when(this.inventoryRepository.existsByProductId(100L)).thenReturn(false);

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> this.inventoryService.createInventory(request)
                );

        assertEquals(
                "Quantity cannot be negative",
                exception.getMessage()
        );
    }

    @Test
    void getInventoryByProductId_shouldReturnInventory() {

        when(this.inventoryRepository.findByProductId(100L)).thenReturn(Optional.of(this.inventory));

        InventoryResponseDTO response = this.inventoryService.getInventoryByProductId(100L);

        assertEquals(100L, response.getProductId());
        assertEquals(20, response.getQuantity());
    }

    @Test
    void getInventoryByProductId_shouldThrowException_whenNotFound() {

        when(this.inventoryRepository.findByProductId(100L)).thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> this.inventoryService.getInventoryByProductId(100L)
                );

        assertEquals(
                "Inventory not found",
                exception.getMessage()
        );
    }

    @Test
    void updateInventory_shouldUpdateQuantity() {

        UpdateInventoryRequestDTO request =
                UpdateInventoryRequestDTO.builder()
                        .quantity(50)
                        .build();

        when(this.inventoryRepository.findByProductId(100L)).thenReturn(Optional.of(this.inventory));

        when(this.inventoryRepository.save(any(Inventory.class))).thenReturn(this.inventory);

        InventoryResponseDTO response = this.inventoryService.updateInventory(100L, request);

        assertEquals(
                50,
                response.getQuantity()
        );

        verify(this.inventoryRepository).save(this.inventory);
    }

    @Test
    void increaseStock_shouldIncreaseQuantity() {

        when(this.inventoryRepository.findByProductId(100L)).thenReturn(Optional.of(this.inventory));

        this.inventoryService.increaseStock(100L, 10);

        assertEquals(
                30,
                this.inventory.getQuantity()
        );

        verify(this.inventoryRepository).save(this.inventory);
    }

    @Test
    void increaseStock_shouldThrowException_whenQuantityZero() {

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> this.inventoryService.increaseStock(100L,0)
                );

        assertEquals(
                "Quantity must be greater than zero",
                exception.getMessage()
        );
    }

    @Test
    void decreaseStock_shouldDecreaseQuantity() {

        when(this.inventoryRepository.findByProductId(100L)).thenReturn(Optional.of(this.inventory));

        this.inventoryService.decreaseStock(100L,5);

        assertEquals(
                15,
                this.inventory.getQuantity()
        );

        verify(this.inventoryRepository).save(this.inventory);
    }

    @Test
    void decreaseStock_shouldThrowException_whenInsufficientStock() {

        when(this.inventoryRepository.findByProductId(100L)).thenReturn(Optional.of(this.inventory));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> this.inventoryService.decreaseStock(100L,50)
                );

        assertEquals(
                "Insufficient stock",
                exception.getMessage()
        );

        verify(this.inventoryRepository, never()).save(any());
    }

    @Test
    void deleteInventory_shouldDeleteSuccessfully() {

        when(this.inventoryRepository.findByProductId(100L))
                .thenReturn(Optional.of(this.inventory));

        this.inventoryService.deleteInventory(100L);

        verify(this.inventoryRepository).delete(this.inventory);
    }
}