package org.example.rentalservice.dto;

import java.time.LocalDate;
import java.util.UUID;

public record RentalRequestDto(
        UUID carUid,
        UUID paymentUid,
        LocalDate dateFrom,
        LocalDate dateTo
) {
}