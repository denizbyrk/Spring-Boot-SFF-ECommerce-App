package com.denizbyrk.sffecommerce.user_service.controller;

import com.denizbyrk.sffecommerce.user_service.service.AuthService;
import com.denizbyrk.sffecommerce.user_service.DTO.LoginRequestDTO;
import com.denizbyrk.sffecommerce.user_service.DTO.LoginResponseDTO;
import com.denizbyrk.sffecommerce.user_service.DTO.RegisterRequestDTO;
import com.denizbyrk.sffecommerce.user_service.DTO.RefreshTokenRequestDTO;
import com.denizbyrk.sffecommerce.user_service.DTO.RefreshTokenResponseDTO;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {

        this.authService = authService;
    }

    ///
    /// register page
    ///
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDTO request) {

        String response = this.authService.register(request);

        return ResponseEntity.ok(response);
    }

    ///
    /// login page
    ///
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {

        LoginResponseDTO response = this.authService.login(request);

        return ResponseEntity.ok(response);
    }

    ///
    /// refresh token
    ///
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponseDTO> refresh(@Valid @RequestBody RefreshTokenRequestDTO request) {

        RefreshTokenResponseDTO response = this.authService.refreshToken(request);

        return ResponseEntity.ok(response);
    }

    ///
    /// logout
    ///
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal UserDetails userDetails) {

        String response = this.authService.logout(userDetails.getUsername());

        return ResponseEntity.ok(response);
    }
}