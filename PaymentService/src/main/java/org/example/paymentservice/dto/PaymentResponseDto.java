package org.example.paymentservice.dto;

import org.example.paymentservice.constants.PaymentStatus;

import java.util.UUID;

public record PaymentResponseDto(
        UUID paymentUid,
        PaymentStatus status,
        int price
) {
}