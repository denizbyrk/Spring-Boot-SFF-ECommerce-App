package com.denizbyrk.sffecommerce.user_service.controller;

import com.denizbyrk.sffecommerce.user_service.DTO.UserResponseDTO;
import com.denizbyrk.sffecommerce.user_service.service.UserService;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Hidden
@RestController
@RequestMapping("/api/internal/users")
public class InternalController {

    private final UserService userService;

    public InternalController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping("/id/{id}")
    public Optional<UserResponseDTO> getUserById(@PathVariable Long id) {

        return this.userService.getUserById(id);
    }

    @GetMapping("/username/{username}")
    public Optional<UserResponseDTO> getUserByUsername(@PathVariable String username) {

        return this.userService.getUserByUsername(username);
    }
}