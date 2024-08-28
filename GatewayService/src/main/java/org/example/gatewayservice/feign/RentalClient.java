package org.example.gatewayservice.feign;

import org.example.gatewayservice.constants.CustomHeaders;
import org.example.gatewayservice.dto.rental.RentalRequestDto;
import org.example.gatewayservice.dto.rental.RentalResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "rental", url = "${feign.rental.url}")
public interface RentalClient {
    @PostMapping("${services.rental.url.base}")
    RentalResponseDto createRental(@RequestHeader(CustomHeaders.USERNAME_HEADER) String username,
                                   @RequestBody RentalRequestDto rentalRequest);

    @GetMapping("${services.rental.url.base}")
    ResponseEntity<List<RentalResponseDto>> getRentals(@RequestHeader(CustomHeaders.USERNAME_HEADER) String username);

    @GetMapping("${services.rental.url.base}/{rentalUid}")
    ResponseEntity<RentalResponseDto> getRental(@PathVariable UUID rentalUid,
                                                @RequestHeader(CustomHeaders.USERNAME_HEADER) String username);

    @DeleteMapping("${services.rental.url.base}/{rentalUid}")
    ResponseEntity<?> cancelRental(@PathVariable UUID rentalUid,
                                   @RequestHeader(CustomHeaders.USERNAME_HEADER) String username);

    @PostMapping("${services.rental.url.base}/{rentalUid}/finish")
    ResponseEntity<?> finishRental(@PathVariable UUID rentalUid,
                                   @RequestHeader(CustomHeaders.USERNAME_HEADER) String username);

}
