package com.denizbyrk.sffecommerce.user_service.controller;

import com.denizbyrk.sffecommerce.user_service.DTO.UserResponseDTO;
import com.denizbyrk.sffecommerce.user_service.service.AdminService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {

        this.adminService = adminService;
    }

    ///
    /// admin dashboard
    ///
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminDashboard() {

        return ResponseEntity.ok("Welcome to admin dashboard");
    }

    ///
    /// get all users
    ///
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        List<UserResponseDTO> users = this.adminService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    ///
    /// get user by id
    ///
    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Optional<UserResponseDTO>> getUserById(@PathVariable Long id) {

        Optional<UserResponseDTO> user = this.adminService.getUserById(id);

        return ResponseEntity.ok(user);
    }

    ///
    /// delete user
    ///
    @DeleteMapping("/users/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {

        return ResponseEntity.ok(this.adminService.deleteUser(id));
    }
}