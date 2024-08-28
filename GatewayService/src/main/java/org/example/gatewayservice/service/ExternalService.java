package org.example.gatewayservice.service;

import org.example.gatewayservice.dto.car.CarDto;
import org.example.gatewayservice.dto.car.CarResponseDto;
import org.example.gatewayservice.dto.payment.PaymentResponseDto;
import org.example.gatewayservice.dto.rental.RentalRequestDto;
import org.example.gatewayservice.dto.rental.RentalResponseDto;
import org.example.gatewayservice.wrapper.FallbackWrapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ExternalService {
    Page<CarDto> getCars(boolean showAll, int page, int size);

    CarResponseDto getCar(UUID carUid);

    boolean changeCarAvailability(UUID carId, boolean availability);

    FallbackWrapper<PaymentResponseDto> createPayment(int totalPrice);

    FallbackWrapper<RentalResponseDto> createRental(String username, RentalRequestDto rentalInDto);

    boolean cancelRental(String username, UUID rentalUid);

    List<RentalResponseDto> getRentals(String username);

    FallbackWrapper<RentalResponseDto> getRental(String username, UUID rentalUid);

    boolean cancelPayment(UUID paymentsUid);

    Map<UUID, PaymentResponseDto> getPayments(List<UUID> pyamentsUids);

    Map<UUID, CarResponseDto> getCars(List<UUID> carUids);

    PaymentResponseDto getPayment(UUID paymentUid);

    boolean finishRental(UUID rentalUid, String username);
}