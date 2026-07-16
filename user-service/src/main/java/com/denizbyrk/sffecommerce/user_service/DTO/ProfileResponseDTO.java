package com.denizbyrk.sffecommerce.user_service.DTO;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ProfileResponseDTO {

    private String username;
    private String email;
    private BigDecimal balance;
    private LocalDateTime createdAt;
}