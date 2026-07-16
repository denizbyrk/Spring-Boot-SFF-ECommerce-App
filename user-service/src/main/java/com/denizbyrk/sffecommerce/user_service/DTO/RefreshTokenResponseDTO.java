package com.denizbyrk.sffecommerce.user_service.DTO;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class RefreshTokenResponseDTO {

    private String accessToken;
    private String refreshToken;
    private String message;
}