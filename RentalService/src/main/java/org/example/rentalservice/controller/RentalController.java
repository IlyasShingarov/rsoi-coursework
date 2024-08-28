package org.example.rentalservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.rentalservice.constants.CustomHeaders;
import org.example.rentalservice.dto.RentalRequestDto;
import org.example.rentalservice.dto.RentalResponseDto;
import org.example.rentalservice.service.RentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("${api.url.base}")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @PostMapping
    RentalResponseDto createRental(@RequestHeader(CustomHeaders.USERNAME_HEADER) String username,
                                   @RequestBody RentalRequestDto rentalInDto) {
        log.info("Request for creating rental for user '{}'", username);
        return rentalService.createRental(username, rentalInDto);
    }

    @GetMapping
    ResponseEntity<List<RentalResponseDto>> getRentals(@RequestHeader(CustomHeaders.USERNAME_HEADER) String username) {
        log.info("Request for reading all rental of user {}", username);
        return ResponseEntity.ok(rentalService.getRentals(username));
    }

    @GetMapping(path = "/{rentalUid}")
    ResponseEntity<RentalResponseDto> getRental(@PathVariable UUID rentalUid,
                                                @RequestHeader(CustomHeaders.USERNAME_HEADER) String username) {
        log.info("Request for reading rental {} of user {}", rentalUid, username);
        var response = rentalService.getRental(rentalUid, username);
        response.ifPresent(res -> log.debug(res.toString()));
        return response
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{rentalUid}")
    ResponseEntity<?> cancelRental(@PathVariable UUID rentalUid,
                                   @RequestHeader(CustomHeaders.USERNAME_HEADER) String username) {
        log.info("Request for cancelling rental '{}' for user '{}'", rentalUid, username);
        rentalService.cancelRental(rentalUid, username);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/{rentalUid}/finish")
    ResponseEntity<?> finishRental(@PathVariable UUID rentalUid,
                                   @RequestHeader(CustomHeaders.USERNAME_HEADER) String username) {
        log.info("Request for finish rental '{}' for user '{}'", rentalUid, username);
        rentalService.finishRental(rentalUid, username);
        return ResponseEntity.noContent().build();
    }
}