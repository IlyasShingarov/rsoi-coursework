package org.example.gatewayservice.dto.payment;

import java.util.UUID;

public record PaymentResponseDto(
        UUID paymentUid,
        PaymentStatus status,
        int price
) {
}