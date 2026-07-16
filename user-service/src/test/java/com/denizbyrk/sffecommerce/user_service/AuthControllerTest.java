package com.denizbyrk.sffecommerce.user_service;

import com.denizbyrk.sffecommerce.user_service.service.AuthService;
import com.denizbyrk.sffecommerce.user_service.DTO.LoginRequestDTO;
import com.denizbyrk.sffecommerce.user_service.filter.JwtAuthFilter;
import com.denizbyrk.sffecommerce.user_service.DTO.LoginResponseDTO;
import com.denizbyrk.sffecommerce.user_service.controller.AuthController;
import com.denizbyrk.sffecommerce.user_service.exception.JwtAuthEntryPoint;
import com.denizbyrk.sffecommerce.user_service.exception.GlobalExceptionHandler;
import com.denizbyrk.sffecommerce.user_service.exception.JwtAccessDeniedHandler;
import com.denizbyrk.sffecommerce.user_service.exception.InvalidCredentialsException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({GlobalExceptionHandler.class})
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private JwtAuthEntryPoint jwtAuthEntryPoint;

    @MockitoBean
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Test
    void shouldLoginSuccessfully() throws Exception {

        LoginRequestDTO request = new LoginRequestDTO("deniz@gmail.com", "password");

        LoginResponseDTO response =
                new LoginResponseDTO(
                        "access-token",
                        "refresh-token",
                        "deniz",
                        "Login successful"
                );

        when(this.authService.login(any(LoginRequestDTO.class)))
                .thenReturn(response);

        this.mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken")
                        .value("access-token"))
                .andExpect(jsonPath("$.refreshToken")
                        .value("refresh-token"))
                .andExpect(jsonPath("$.username")
                        .value("deniz"))
                .andExpect(jsonPath("$.message")
                        .value("Login successful"));
    }

    @Test
    void shouldReturnUnauthorizedWhenLoginFails() throws Exception {

        LoginRequestDTO request = new LoginRequestDTO("deniz@gmail.com", "wrongpassword");

        when(this.authService.login(any(LoginRequestDTO.class)))
                .thenThrow(new InvalidCredentialsException(
                        "Invalid email or password"));


        this.mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message")
                        .value("Invalid email or password"));
    }
}