package com.denizbyrk.sffecommerce.inventory_service.event;

import com.denizbyrk.sffecommerce.inventory_service.entity.Inventory;
import com.denizbyrk.sffecommerce.inventory_service.repository.InventoryRepository;

import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

@Component
public class ProductCreatedListener {

    private final InventoryRepository inventoryRepository;

    public ProductCreatedListener(InventoryRepository inventoryRepository) {

        this.inventoryRepository = inventoryRepository;
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {

        return new Jackson2JsonMessageConverter();
    }

    @RabbitListener(queues = "product.created.queue")
    public void handleProductCreated(
            ProductCreatedEvent event) {

        Inventory inventory = Inventory.builder()
                .productId(event.getProductId())
                .quantity(1)
                .build();

        this.inventoryRepository.save(inventory);

        System.out.println("Inventory created for product: " + event.getProductId());
    }
}