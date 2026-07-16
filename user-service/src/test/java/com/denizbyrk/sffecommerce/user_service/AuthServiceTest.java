package com.denizbyrk.sffecommerce.user_service;

import com.denizbyrk.sffecommerce.user_service.DTO.LoginRequestDTO;
import com.denizbyrk.sffecommerce.user_service.DTO.LoginResponseDTO;
import com.denizbyrk.sffecommerce.user_service.DTO.RegisterRequestDTO;
import com.denizbyrk.sffecommerce.user_service.DTO.RefreshTokenRequestDTO;
import com.denizbyrk.sffecommerce.user_service.DTO.RefreshTokenResponseDTO;

import com.denizbyrk.sffecommerce.user_service.entity.Role;
import com.denizbyrk.sffecommerce.user_service.entity.User;
import com.denizbyrk.sffecommerce.user_service.entity.RefreshToken;

import com.denizbyrk.sffecommerce.user_service.util.JwtUtil;
import com.denizbyrk.sffecommerce.user_service.service.AuthService;
import com.denizbyrk.sffecommerce.user_service.repository.UserRepository;
import com.denizbyrk.sffecommerce.user_service.service.RefreshTokenService;

import java.time.Instant;
import java.util.Optional;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthService authService;

    ///
    /// test register
    ///
    @Test
    void shouldRegisterSuccessfully() {

        RegisterRequestDTO request = new RegisterRequestDTO("deniz", "deniz@gmail.com", "password");

        when(this.userRepository.existsByUsername("deniz"))
                .thenReturn(false);

        when(this.userRepository.existsByEmail("deniz@gmail.com"))
                .thenReturn(false);

        when(this.passwordEncoder.encode("password"))
                .thenReturn("encodedPassword");

        User savedUser = User.builder()
                .username("deniz")
                .email("deniz@gmail.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(this.userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        String result = this.authService.register(request);


        assertEquals("Account created successfully", result);

        verify(this.userRepository).save(any(User.class));
    }

    ///
    /// test username existing
    ///
    @Test
    void shouldFailWhenUsernameExists(){

        RegisterRequestDTO request =
                new RegisterRequestDTO("deniz", "email@test.com", "password");

        when(this.userRepository.existsByUsername("deniz"))
                .thenReturn(true);

        assertThrows(
                RuntimeException.class,
                () -> this.authService.register(request)
        );

        verify(this.userRepository, never()).save(any());
    }

    ///
    /// test email existing
    ///
    @Test
    void shouldFailWhenEmailExists(){

        RegisterRequestDTO request =
                new RegisterRequestDTO("deniz", "email@test.com", "password");

        when(this.userRepository.existsByEmail("email@test.com"))
                .thenReturn(true);

        assertThrows(
                RuntimeException.class,
                () -> this.authService.register(request)
        );

        verify(this.userRepository, never()).save(any());
    }

    ///
    /// test login
    ///
    @Test
    void shouldLoginSuccessfully(){

        User user = User.builder()
                .username("email@test.com")
                .password("encoded")
                .role(Role.USER)
                .build();

        LoginRequestDTO request =
                new LoginRequestDTO(
                        "email@test.com",
                        "password"
                );

        when(this.userRepository.findByEmail("email@test.com"))
                .thenReturn(Optional.of(user));


        when(this.passwordEncoder.matches(
                "password",
                "encoded"
        ))
                .thenReturn(true);

        when(this.jwtUtil.generateToken(user.getUsername()))
                .thenReturn("jwt-token");

        RefreshToken refreshToken = RefreshToken.builder()
                .token("refresh-token")
                .user(user)
                .expireDate(Instant.now().plusSeconds(3600))
                .createdAt(Instant.now())
                .build();

        when(this.refreshTokenService.createRefreshToken(user.getUsername()))
                .thenReturn(refreshToken);

        LoginResponseDTO response = this.authService.login(request);

        assertEquals(
                "jwt-token",
                response.getAccessToken()
        );
    }

    ///
    /// test wrong password
    ///
    @Test
    void shouldFailWithWrongPassword(){

        User user = User.builder()
                .password("encoded")
                .build();

        when(this.userRepository.findByEmail("email@test.com"))
                .thenReturn(Optional.of(user));

        when(this.passwordEncoder.matches(
                anyString(),
                anyString()
        ))
                .thenReturn(false);

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                this.authService.login(
                                        new LoginRequestDTO(
                                                "email@test.com",
                                                "wrong")
                                )
                );

        assertEquals(
                "Invalid email or password",
                exception.getMessage()
        );
    }

    ///
    /// test refresh token
    ///
    @Test
    void shouldRefreshTokenSuccessfully() {

        User user = User.builder()
                .username("deniz")
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        RefreshToken refreshToken = RefreshToken.builder()
                .token("old-token")
                .user(user)
                .expireDate(Instant.now().plusSeconds(3600))
                .createdAt(Instant.now())
                .build();

        RefreshTokenRequestDTO request =
                new RefreshTokenRequestDTO("old-token");

        when(this.refreshTokenService.findByToken("old-token"))
                .thenReturn(Optional.of(refreshToken));

        when(this.refreshTokenService.verifyExpiration(refreshToken))
                .thenReturn(refreshToken);

        when(this.jwtUtil.generateToken("deniz"))
                .thenReturn("new-access-token");

        RefreshToken newRefreshToken = RefreshToken.builder()
                .token("new-refresh-token")
                .user(user)
                .expireDate(Instant.now().plusSeconds(3600))
                .createdAt(Instant.now())
                .build();

        when(this.refreshTokenService.createRefreshToken("deniz"))
                .thenReturn(newRefreshToken);

        RefreshTokenResponseDTO response = this.authService.refreshToken(request);

        assertEquals(
                "new-access-token",
                response.getAccessToken()
        );

        assertEquals(
                "new-refresh-token",
                response.getRefreshToken()
        );

        assertEquals(
                "Refresh successfull",
                response.getMessage()
        );
    }

    ///
    /// test refresh token already exist
    ///
    @Test
    void shouldFailWhenRefreshTokenDoesNotExist(){

        RefreshTokenRequestDTO request =
                new RefreshTokenRequestDTO("invalid-token");

        when(this.refreshTokenService.findByToken("invalid-token"))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> this.authService.refreshToken(request)
                );

        assertEquals(
                "Invalid refresh token",
                exception.getMessage()
        );
    }
}