package com.denizbyrk.sffecommerce.user_service.DTO;

import com.denizbyrk.sffecommerce.user_service.entity.Role;
import com.denizbyrk.sffecommerce.user_service.entity.UserStatus;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private Long id;
    private String username;
    private String email;
    private Role role;
    private BigDecimal balance;
    private UserStatus userStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}