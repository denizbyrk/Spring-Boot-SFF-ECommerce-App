package com.denizbyrk.sffecommerce.inventory_service.config;

import com.denizbyrk.sffecommerce.inventory_service.entity.Inventory;
import com.denizbyrk.sffecommerce.inventory_service.repository.InventoryRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initInventory(InventoryRepository inventoryRepository) {

        return args -> {

            if (inventoryRepository.count() > 0) {
                return;
            }

            inventoryRepository.save(Inventory.builder()
                    .productId(1L)
                    .quantity(20)
                    .build());

            inventoryRepository.save(Inventory.builder()
                    .productId(2L)
                    .quantity(2500)
                    .build());

            inventoryRepository.save(Inventory.builder()
                    .productId(3L)
                    .quantity(300)
                    .build());

            inventoryRepository.save(Inventory.builder()
                    .productId(4L)
                    .quantity(120)
                    .build());

            inventoryRepository.save(Inventory.builder()
                    .productId(5L)
                    .quantity(80)
                    .build());

            inventoryRepository.save(Inventory.builder()
                    .productId(6L)
                    .quantity(75)
                    .build());

            inventoryRepository.save(Inventory.builder()
                    .productId(7L)
                    .quantity(40)
                    .build());

            inventoryRepository.save(Inventory.builder()
                    .productId(8L)
                    .quantity(150)
                    .build());

            inventoryRepository.save(Inventory.builder()
                    .productId(9L)
                    .quantity(140)
                    .build());

            inventoryRepository.save(Inventory.builder()
                    .productId(10L)
                    .quantity(250)
                    .build());

            inventoryRepository.save(Inventory.builder()
                    .productId(11L)
                    .quantity(180)
                    .build());

            inventoryRepository.save(Inventory.builder()
                    .productId(12L)
                    .quantity(25)
                    .build());

            inventoryRepository.save(Inventory.builder()
                    .productId(13L)
                    .quantity(35)
                    .build());

            inventoryRepository.save(Inventory.builder()
                    .productId(14L)
                    .quantity(45)
                    .build());

            inventoryRepository.save(Inventory.builder()
                    .productId(15L)
                    .quantity(90)
                    .build());

            inventoryRepository.save(Inventory.builder()
                    .productId(16L)
                    .quantity(110)
                    .build());

            inventoryRepository.save(Inventory.builder()
                    .productId(17L)
                    .quantity(95)
                    .build());

            inventoryRepository.save(Inventory.builder()
                    .productId(18L)
                    .quantity(70)
                    .build());

            inventoryRepository.save(Inventory.builder()
                    .productId(19L)
                    .quantity(60)
                    .build());

            inventoryRepository.save(Inventory.builder()
                    .productId(20L)
                    .quantity(85)
                    .build());
        };
    }
}