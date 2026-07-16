package com.denizbyrk.sffecommerce.inventory_service.service;

import com.denizbyrk.sffecommerce.inventory_service.entity.Inventory;
import com.denizbyrk.sffecommerce.inventory_service.client.ProductClient;
import com.denizbyrk.sffecommerce.inventory_service.DTO.InventoryResponseDTO;
import com.denizbyrk.sffecommerce.inventory_service.DTO.CreateInventoryRequestDTO;
import com.denizbyrk.sffecommerce.inventory_service.DTO.UpdateInventoryRequestDTO;
import com.denizbyrk.sffecommerce.inventory_service.repository.InventoryRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductClient productClient;

    public InventoryService(InventoryRepository inventoryRepository, ProductClient productClient) {

        this.inventoryRepository = inventoryRepository;
        this.productClient = productClient;
    }

    ///
    /// create inventory
    ///
    public InventoryResponseDTO createInventory(CreateInventoryRequestDTO request) {

        this.productClient.getProductById(request.getProductId());

        if (this.inventoryRepository.existsByProductId(request.getProductId())) {
            throw new RuntimeException("Inventory already exists for this product");
        }

        if (request.getQuantity() < 0) {
            throw new RuntimeException("Quantity cannot be negative");
        }

        Inventory inventory = Inventory.builder()
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .build();

        this.inventoryRepository.save(inventory);

        return mapToResponse(inventory);
    }

    ///
    /// get all
    ///
    @Transactional(readOnly = true)
    public List<InventoryResponseDTO> getAllInventory() {

        return this.inventoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    ///
    /// get by id
    ///
    @Transactional(readOnly = true)
    public InventoryResponseDTO getInventoryByProductId(Long productId) {

        Inventory inventory = this.inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        return mapToResponse(inventory);
    }

    ///
    /// update
    ///
    public InventoryResponseDTO updateInventory(Long productId,
                                                UpdateInventoryRequestDTO request) {

        Inventory inventory = this.inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        if (request.getQuantity() < 0) {
            throw new RuntimeException("Quantity cannot be negative");
        }

        inventory.setQuantity(request.getQuantity());

        this.inventoryRepository.save(inventory);

        return mapToResponse(inventory);
    }

    ///
    /// increase stock
    ///
    public void increaseStock(Long productId, Integer quantity) {

        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than zero");
        }

        Inventory inventory = this.inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        inventory.setQuantity(inventory.getQuantity() + quantity);

        this.inventoryRepository.save(inventory);
    }

    ///
    /// decrease stock
    ///
    public void decreaseStock(Long productId, Integer quantity) {

        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than zero");
        }

        Inventory inventory = this.inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        if (inventory.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);

        this.inventoryRepository.save(inventory);
    }

    ///
    /// delete inventory
    ///
    public void deleteInventory(Long productId) {

        Inventory inventory = this.inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        this.inventoryRepository.delete(inventory);
    }

    ///
    /// map
    ///
    private InventoryResponseDTO mapToResponse(Inventory inventory) {

        return InventoryResponseDTO.builder()
                .productId(inventory.getProductId())
                .quantity(inventory.getQuantity())
                .build();
    }
}