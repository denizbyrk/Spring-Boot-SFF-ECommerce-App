package com.denizbyrk.sffecommerce.user_service.service;

import com.denizbyrk.sffecommerce.user_service.entity.User;
import com.denizbyrk.sffecommerce.user_service.DTO.UserResponseDTO;
import com.denizbyrk.sffecommerce.user_service.DTO.ProfileResponseDTO;
import com.denizbyrk.sffecommerce.user_service.repository.UserRepository;
import com.denizbyrk.sffecommerce.user_service.exception.UserNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public ProfileResponseDTO getProfileDetails() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = this.userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        ProfileResponseDTO response = new ProfileResponseDTO(user.getUsername(), user.getEmail(), user.getBalance(), user.getCreatedAt());

        return response;
    }

    public Optional<UserResponseDTO> getUserById(Long id) {

        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("id not found"));

        return Optional.of(new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getBalance(),
                user.getUserStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        ));
    }

    public Optional<UserResponseDTO> getUserByUsername(String username) {

        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("id not found"));

        return Optional.of(new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getBalance(),
                user.getUserStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        ));
    }
}