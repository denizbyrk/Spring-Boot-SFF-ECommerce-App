package com.denizbyrk.sffecommerce.payment_service.DTO;

import com.denizbyrk.sffecommerce.payment_service.entity.PaymentMethod;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class PaymentRequestDTO {

    private Long orderId;

    private PaymentMethod paymentMethod;

    private String cardHolderName;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
}
