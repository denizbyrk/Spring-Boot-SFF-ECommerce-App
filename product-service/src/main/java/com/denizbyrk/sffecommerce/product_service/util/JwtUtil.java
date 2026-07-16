package com.denizbyrk.sffecommerce.product_service.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import java.security.Key;

@Component
public class JwtUtil {

    private final String secret;
    private final long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration
    ) {

        this.secret = secret;
        this.accessTokenExpiration = accessTokenExpiration;
    }

    /// get the signing key
    private Key getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(this.secret);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    //generate token
    public String generateToken(String username) {

        Map<String, Object> claims = new HashMap<>();

        return createToken(claims, username);
    }

    //create token
    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + this.accessTokenExpiration))
                .signWith(this.getSigningKey())
                .compact();
    }

    //extract claims
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .setSigningKey(this.getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //extract username
    public String extractUsername(String token) {

        return this.extractAllClaims(token).getSubject();
    }

    //extract expiration
    public Date extractExpiration(String token) {

        return this.extractAllClaims(token).getExpiration();
    }

    //check if the token is expired
    private boolean isTokenExpired(String token) {

        return this.extractAllClaims(token).getExpiration().before(new Date());
    }

    //validate token
    public boolean validateToken(String token, String username) {

        final String extractedUsername = extractUsername(token);

        return (extractedUsername.equals(username) && ! isTokenExpired(token));
    }
}