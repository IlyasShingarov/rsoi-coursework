package org.example.carservice.dto;

import org.example.carservice.domain.CarType;

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