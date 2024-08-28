package org.example.rentalservice.service;

import org.example.rentalservice.dto.RentalRequestDto;
import org.example.rentalservice.dto.RentalResponseDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RentalService {
    RentalResponseDto createRental(String username, RentalRequestDto request);

    List<RentalResponseDto> getRentals(String username);

    Optional<RentalResponseDto> getRental(UUID rentalUid, String username);

    void cancelRental(UUID rentalUid, String username);

    void finishRental(UUID rentalUid, String username);
}
