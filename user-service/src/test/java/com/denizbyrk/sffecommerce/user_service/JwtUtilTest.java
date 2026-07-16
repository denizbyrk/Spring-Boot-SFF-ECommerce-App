package com.denizbyrk.sffecommerce.user_service;

import com.denizbyrk.sffecommerce.user_service.util.JwtUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JwtUtilTest {

    private JwtUtil jwtUtil;

    private static final String SECRET = "c78ba16276efe5d5bc1661ff10ec1192b4fa364dc9147f60c76d3a71a0e5d960c68a5fad0ceee979c1faa5e3211c9397b6708eeb4ed01bb8d5f1641c6857d86a";

    ///
    /// setup
    ///
    @BeforeEach
    void setUp() {

        this.jwtUtil = new JwtUtil(JwtUtilTest.SECRET, 3600000L);
    }

    ///
    /// test generate token
    ///
    @Test
    void shouldGenerateToken() {

        String token = this.jwtUtil.generateToken("deniz");

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    ///
    /// get username
    ///
    @Test
    void shouldExtractUsername() {

        String token = this.jwtUtil.generateToken("deniz");
        String username = this.jwtUtil.extractUsername(token);

        assertEquals("deniz", username);
    }

    ///
    /// validate token
    ///
    @Test
    void shouldValidateToken() {

        String token = this.jwtUtil.generateToken("deniz");

        assertTrue(
                this.jwtUtil.validateToken(token, "deniz")
        );
    }

    ///
    /// test invalid username
    ///
    @Test
    void shouldRejectInvalidUsername() {

        String token = this.jwtUtil.generateToken("deniz");

        assertFalse(
                this.jwtUtil.validateToken(token, "dinez")
        );
    }

    ///
    /// get expiration
    ///
    @Test
    void shouldExtractExpiration() {

        String token = this.jwtUtil.generateToken("deniz");

        assertNotNull(
                this.jwtUtil.extractExpiration(token)
        );
    }
}