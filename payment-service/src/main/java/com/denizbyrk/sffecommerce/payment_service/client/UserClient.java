package com.denizbyrk.sffecommerce.payment_service.client;

import com.denizbyrk.sffecommerce.payment_service.config.FeignConfig;
import com.denizbyrk.sffecommerce.payment_service.DTO.UserResponseDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", configuration = FeignConfig.class)
public interface UserClient {

    @GetMapping("/api/internal/users/id/{id}")
    UserResponseDTO getUserById(@PathVariable("id") Long id);

    @GetMapping("/api/internal/users/username/{username}")
    UserResponseDTO getUserByUsername(@PathVariable("username") String username);
}