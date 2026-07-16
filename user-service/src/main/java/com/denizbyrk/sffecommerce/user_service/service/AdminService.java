package com.denizbyrk.sffecommerce.user_service.service;

import com.denizbyrk.sffecommerce.user_service.entity.User;
import com.denizbyrk.sffecommerce.user_service.DTO.UserResponseDTO;
import com.denizbyrk.sffecommerce.user_service.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public List<UserResponseDTO> getAllUsers() {

        List<User> users = this.userRepository.findAll();

        List<UserResponseDTO> response = new ArrayList<>();

        for(User user : users) {

            UserResponseDTO userResponseDTO = new UserResponseDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole(),
                    user.getBalance(),
                    user.getUserStatus(),
                    user.getCreatedAt(),
                    user.getUpdatedAt()
            );

            response.add(userResponseDTO);
        }

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

    public String deleteUser(Long id) {

        this.userRepository.deleteById(id);

        return "User ID " + id + " successfully deleted";
    }
}