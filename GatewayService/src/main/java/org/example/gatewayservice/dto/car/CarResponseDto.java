package org.example.gatewayservice.dto.car;

import java.util.UUID;

public record CarResponseDto(
        UUID carUid,
        String brand,
        String model,
        String registrationNumber,
        int power,
        CarType type,
        int price,
        boolean available
) {
}