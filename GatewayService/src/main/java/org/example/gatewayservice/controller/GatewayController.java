package org.example.gatewayservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gatewayservice.dto.CarRentDto;
import org.example.gatewayservice.dto.car.CarDto;
import org.example.gatewayservice.dto.rental.RentalCreateDto;
import org.example.gatewayservice.dto.rental.RentalDto;
import org.example.gatewayservice.dto.wrapper.PageableWrapperDto;
import org.example.gatewayservice.exception.ServiceUnavailableException;
import org.example.gatewayservice.service.GatewayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.example.gatewayservice.constants.CustomHeaders.USERNAME_HEADER;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GatewayController {

    private final GatewayService gatewayService;

    @GetMapping("${services.cars.url.base}")
    PageableWrapperDto<CarDto> getAllCars(@RequestParam boolean showAll,
                                          @RequestParam int page,
                                          @RequestParam int size) {
        log.info("Request for reading cars. Request params: showAll {}, page {}, size {}", showAll, page, showAll);

        return gatewayService.getAllCars(showAll, page, size);
    }

    @GetMapping("${services.rental.url.base}")
    ResponseEntity<List<RentalDto>> getRental(@RequestHeader(USERNAME_HEADER) String username) {
        log.info("Request for reading all rental info of user {}", username);

        return ResponseEntity.ok(gatewayService.getRental(username));
    }

    @GetMapping("${services.rental.url.base}/{rentalUid}")
    ResponseEntity<RentalDto> getRental(@RequestHeader(USERNAME_HEADER) String username,
                                        @PathVariable UUID rentalUid) {
        log.info("Request for reading user's {} rental {}", username, rentalUid);

        return ResponseEntity.ok(gatewayService.getRental(username, rentalUid));
    }

    @PostMapping("${services.rental.url.base}")
    ResponseEntity<RentalCreateDto> bookCar(@RequestHeader(USERNAME_HEADER) String userName,
                                            @RequestBody @Valid CarRentDto carBookDto) {
        log.info("Request for booking car {} by user {}", carBookDto, userName);

        RentalCreateDto rentalCreationOutDto = gatewayService.bookCar(userName, carBookDto);

        return ResponseEntity.ok(rentalCreationOutDto);
    }

    @PostMapping("${services.rental.url.base}/{rentalUid}/finish")
    ResponseEntity<?> finishRental(@RequestHeader(USERNAME_HEADER) String username,
                                   @PathVariable UUID rentalUid) {
        log.info("Request for finishing rental {} of user {}", rentalUid, username);

        gatewayService.finishRental(username, rentalUid);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("${services.rental.url.base}/{rentalUid}")
    ResponseEntity<?> cancelRental(@RequestHeader(USERNAME_HEADER) String username,
                                   @PathVariable UUID rentalUid) {
        log.info("Request for canceling rental {} of user {}", rentalUid, username);

        gatewayService.cancelRental(username, rentalUid);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<?> serviceUnavailable(RuntimeException e) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put("Content-Type", List.of("application/json"));
        return new ResponseEntity<>(Map.of("message", e.getMessage()), headers, HttpStatus.SERVICE_UNAVAILABLE);
    }
}

