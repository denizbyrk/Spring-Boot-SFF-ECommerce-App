package com.denizbyrk.sffecommerce.user_service.service;

import com.denizbyrk.sffecommerce.user_service.entity.User;
import com.denizbyrk.sffecommerce.user_service.entity.Role;
import com.denizbyrk.sffecommerce.user_service.util.JwtUtil;
import com.denizbyrk.sffecommerce.user_service.DTO.LoginRequestDTO;
import com.denizbyrk.sffecommerce.user_service.entity.RefreshToken;
import com.denizbyrk.sffecommerce.user_service.DTO.LoginResponseDTO;
import com.denizbyrk.sffecommerce.user_service.DTO.RegisterRequestDTO;
import com.denizbyrk.sffecommerce.user_service.event.UserCreatedEvent;
import com.denizbyrk.sffecommerce.user_service.repository.UserRepository;
import com.denizbyrk.sffecommerce.user_service.DTO.RefreshTokenRequestDTO;
import com.denizbyrk.sffecommerce.user_service.DTO.RefreshTokenResponseDTO;
import com.denizbyrk.sffecommerce.user_service.producer.NotificationProducer;
import com.denizbyrk.sffecommerce.user_service.exception.UserNotFoundException;
import com.denizbyrk.sffecommerce.user_service.exception.DuplicateUserException;
import com.denizbyrk.sffecommerce.user_service.exception.InvalidCredentialsException;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final NotificationProducer notificationProducer;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
                       RefreshTokenService refreshTokenService, NotificationProducer notificationProducer) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.notificationProducer = notificationProducer;
    }

    //register
    public String register(RegisterRequestDTO requestDTO) {

        if (this.userRepository.existsByUsername(requestDTO.getUsername())) {

            throw new DuplicateUserException("Username already exists");
        }

        if (this.userRepository.existsByEmail(requestDTO.getEmail())) {

            throw new DuplicateUserException("Email already exists");
        }

        User user = new User();
        user.setUsername(requestDTO.getUsername());
        user.setEmail(requestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        user.setRole(Role.USER);

        this.userRepository.save(user);

        this.notificationProducer.sendUserCreatedEvent(
                UserCreatedEvent.builder()
                        .userId(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .build()
        );

        return "Account created successfully";
    }

    //login
    public LoginResponseDTO login(LoginRequestDTO request) {

        User user = this.userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!this.passwordEncoder.matches(request.getPassword(), user.getPassword())) {

            throw new InvalidCredentialsException("Invalid email or password");
        }

        String accessToken = this.jwtUtil.generateToken(user.getUsername());
        RefreshToken refreshToken = this.refreshTokenService.createRefreshToken(user.getUsername());

        return new LoginResponseDTO(accessToken, refreshToken.getToken(), user.getUsername(), "Successfully logged in");
    }

    public RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO request) {

        String requestRefreshToken = request.getRefreshToken();

        return this.refreshTokenService.findByToken(requestRefreshToken)
                .map(this.refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = this.jwtUtil.generateToken(((User) user).getUsername());
                    RefreshToken newRefreshToken = this.refreshTokenService.createRefreshToken(((User) user).getUsername());

                    return new RefreshTokenResponseDTO(accessToken, newRefreshToken.getToken(), "Refresh successfull");
                })
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
    }

    public String logout(String username) {

        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Invalid username"));

        this.refreshTokenService.deleteByUserId(user.getId());

        return "Logout successful";
    }
}