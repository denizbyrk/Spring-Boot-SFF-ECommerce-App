package com.denizbyrk.sffecommerce.order_service.DTO;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class UserResponseDTO {

    private Long id;
    private String username;
    private String email;
}