package com.denizbyrk.sffecommerce.user_service.service;

import com.denizbyrk.sffecommerce.user_service.entity.User;
import com.denizbyrk.sffecommerce.user_service.entity.RefreshToken;
import com.denizbyrk.sffecommerce.user_service.repository.UserRepository;
import com.denizbyrk.sffecommerce.user_service.exception.UserNotFoundException;
import com.denizbyrk.sffecommerce.user_service.repository.RefreshTokenRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;
import java.time.Instant;
import java.util.Optional;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {

        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshToken createRefreshToken(String username) {

        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expireDate(Instant.now().plusMillis(refreshTokenExpiration))
                .createdAt(Instant.now())
                .build();

        return this.refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {

        if (token.getExpireDate().isBefore(Instant.now())) {

            this.refreshTokenRepository.delete(token);

            throw new RuntimeException("Token is expired, please login again");
        }

        return token;
    }

    public Optional<RefreshToken> findByToken(String token) {

        return this.refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public void deleteByUserId(Long userId) {

        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        this.refreshTokenRepository.deleteByUser(user);
    }
}