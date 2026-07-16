package com.denizbyrk.sffecommerce.notification_service.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedEvent {

    private Long userId;

    private String username;

    private String email;
}