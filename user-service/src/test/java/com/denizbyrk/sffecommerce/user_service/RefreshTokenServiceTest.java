package com.denizbyrk.sffecommerce.user_service;

import com.denizbyrk.sffecommerce.user_service.entity.User;
import com.denizbyrk.sffecommerce.user_service.entity.RefreshToken;
import com.denizbyrk.sffecommerce.user_service.repository.UserRepository;
import com.denizbyrk.sffecommerce.user_service.service.RefreshTokenService;
import com.denizbyrk.sffecommerce.user_service.repository.RefreshTokenRepository;

import java.time.Instant;
import java.util.Optional;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    ///
    /// create token
    ///
    @Test
    void shouldCreateRefreshTokenSuccessfully() {

        User user = User.builder()
                .username("deniz")
                .build();

        when(this.userRepository.findByUsername("deniz"))
                .thenReturn(Optional.of(user));

        when(this.refreshTokenRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ReflectionTestUtils.setField(
                this.refreshTokenService,
                "refreshTokenExpiration",
                3600000L
        );

        RefreshToken token = this.refreshTokenService.createRefreshToken("deniz");

        assertNotNull(token);

        assertEquals(
                user,
                token.getUser()
        );

        assertNotNull(
                token.getToken()
        );

        assertNotNull(
                token.getExpireDate()
        );

        verify(this.refreshTokenRepository).save(any());
    }

    ///
    /// test if user exists
    ///
    @Test
    void shouldFailWhenUserDoesNotExist(){

        when(this.userRepository.findByUsername("deniz"))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                this.refreshTokenService
                                        .createRefreshToken("deniz")
                );

        assertEquals(
                "User not found",
                exception.getMessage()
        );
    }

    ///
    /// test token expire
    ///
    @Test
    void shouldThrowWhenTokenExpired(){

        RefreshToken token =
                RefreshToken.builder()
                        .expireDate(
                                Instant.now().minusSeconds(60)
                        )
                        .build();

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                this.refreshTokenService
                                        .verifyExpiration(token)
                );

        assertEquals(
                "Token is expired, please login again",
                exception.getMessage()
        );

        verify(this.refreshTokenRepository).delete(token);
    }

    ///
    /// test token return
    ///
    @Test
    void shouldReturnTokenWhenStillValid(){

        RefreshToken token =
                RefreshToken.builder()
                        .expireDate(
                                Instant.now().plusSeconds(3600)
                        )
                        .build();

        RefreshToken result = this.refreshTokenService.verifyExpiration(token);

        assertEquals(
                token,
                result
        );

        verify(this.refreshTokenRepository, never()).delete(any());
    }

    ///
    /// find token
    ///
    @Test
    void shouldFindToken(){

        RefreshToken token = RefreshToken.builder()
                        .token("abc")
                        .build();

        when(this.refreshTokenRepository.findByToken("abc"))
                .thenReturn(Optional.of(token));

        Optional<RefreshToken> result = this.refreshTokenService.findByToken("abc");

        assertTrue(result.isPresent());

        assertEquals(
                token,
                result.get()
        );
    }

    ///
    /// delete by user
    ///
    @Test
    void shouldDeleteTokensByUser(){

        User user = User.builder()
                        .id(1L)
                        .build();

        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));

        this.refreshTokenService.deleteByUserId(1L);

        verify(this.refreshTokenRepository).deleteByUser(user);
    }
}