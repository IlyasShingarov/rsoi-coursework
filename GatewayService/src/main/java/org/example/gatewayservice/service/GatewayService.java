package org.example.gatewayservice.service;

import org.example.gatewayservice.dto.CarRentDto;
import org.example.gatewayservice.dto.car.CarDto;
import org.example.gatewayservice.dto.rental.RentalCreateDto;
import org.example.gatewayservice.dto.rental.RentalDto;
import org.example.gatewayservice.dto.wrapper.PageableWrapperDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface GatewayService {
    PageableWrapperDto<CarDto> getAllCars(boolean showAll, int page, int size);

    List<RentalDto> getRental(String username);

    RentalDto getRental(String username, UUID rentalUid);

    RentalCreateDto bookCar(String userName, CarRentDto carRentDto);

    boolean finishRental(String username, UUID rentalUid);

    boolean cancelRental(String username, UUID rentalUid);
}