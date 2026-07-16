package com.denizbyrk.sffecommerce.user_service.controller;

import com.denizbyrk.sffecommerce.user_service.service.UserService;
import com.denizbyrk.sffecommerce.user_service.DTO.ProfileResponseDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    ///
    /// profile page
    ///
    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ProfileResponseDTO> getUserProfile() {

        ProfileResponseDTO response = userService.getProfileDetails();

        return ResponseEntity.ok(response);
    }
}