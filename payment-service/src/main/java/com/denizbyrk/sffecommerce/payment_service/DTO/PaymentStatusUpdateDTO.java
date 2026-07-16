package com.denizbyrk.sffecommerce.payment_service.DTO;

import com.denizbyrk.sffecommerce.payment_service.entity.PaymentStatus;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class PaymentStatusUpdateDTO {

    private PaymentStatus status;
}