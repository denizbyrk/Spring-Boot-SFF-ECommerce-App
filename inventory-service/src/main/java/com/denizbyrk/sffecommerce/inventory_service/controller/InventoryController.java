package com.denizbyrk.sffecommerce.inventory_service.controller;

import com.denizbyrk.sffecommerce.inventory_service.DTO.InventoryResponseDTO;
import com.denizbyrk.sffecommerce.inventory_service.service.InventoryService;
import com.denizbyrk.sffecommerce.inventory_service.DTO.UpdateInventoryRequestDTO;
import com.denizbyrk.sffecommerce.inventory_service.DTO.CreateInventoryRequestDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {

        this.inventoryService = inventoryService;
    }

    ///
    /// create inventory
    ///
    @PostMapping
    public ResponseEntity<InventoryResponseDTO> createInventory(@RequestBody CreateInventoryRequestDTO request) {

        InventoryResponseDTO response = inventoryService.createInventory(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    ///
    /// get all stocks
    ///
    @GetMapping
    public ResponseEntity<List<InventoryResponseDTO>> getAllInventory() {

        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    ///
    /// get by product id
    ///
    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponseDTO> getInventoryByProductId(@PathVariable Long productId) {

        return ResponseEntity.ok(inventoryService.getInventoryByProductId(productId));
    }

    ///
    /// update stock
    ///
    @PutMapping("/{productId}")
    public ResponseEntity<InventoryResponseDTO> updateInventory(@PathVariable Long productId, @RequestBody UpdateInventoryRequestDTO request) {

        return ResponseEntity.ok(inventoryService.updateInventory(productId, request));
    }

    ///
    /// increase stock
    ///
    @PatchMapping("/{productId}/increase")
    public ResponseEntity<Void> increaseStock(@PathVariable Long productId, @RequestParam Integer quantity) {

        inventoryService.increaseStock(productId, quantity);

        return ResponseEntity.ok().build();
    }

    ///
    /// decrease stock
    ///
    @PatchMapping("/{productId}/decrease")
    public ResponseEntity<Void> decreaseStock(@PathVariable Long productId, @RequestParam Integer quantity) {

        inventoryService.decreaseStock(productId, quantity);

        return ResponseEntity.ok().build();
    }

    ///
    /// delete
    ///
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long productId) {

        inventoryService.deleteInventory(productId);

        return ResponseEntity.noContent().build();
    }
}