package org.example.rentalservice.dto;

import lombok.Data;
import org.example.rentalservice.constants.RentalStatus;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class RentalResponseDto {
    private UUID rentalUid;
    private String username;
    private UUID paymentUid;
    private UUID carUid;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private RentalStatus status;
}
