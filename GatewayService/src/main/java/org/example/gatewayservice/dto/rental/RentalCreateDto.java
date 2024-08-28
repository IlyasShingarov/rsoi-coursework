package org.example.gatewayservice.dto.rental;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.example.gatewayservice.dto.payment.PaymentDto;

import java.time.LocalDate;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RentalCreateDto {
    UUID rentalUid;
    RentalStatus status;
    LocalDate dateFrom;
    LocalDate dateTo;
    UUID carUid;
    PaymentDto payment;
}
