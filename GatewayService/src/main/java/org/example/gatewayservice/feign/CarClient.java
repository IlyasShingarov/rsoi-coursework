package org.example.gatewayservice.feign;

import org.example.gatewayservice.dto.car.CarResponseDto;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "cars", url = "${feign.cars.url}")
public interface CarClient {
    @GetMapping("${services.cars.url.base}")
    Page<CarResponseDto> getCars(@RequestParam @DefaultValue("false") boolean showAll,
                                 @RequestParam @DefaultValue("0") int page,
                                 @RequestParam @DefaultValue("100") int size);

    @GetMapping("${services.cars.url.base}/{carId}")
    CarResponseDto getCar(@PathVariable UUID carId);

    @PostMapping("${services.cars.url.base}")
    List<CarResponseDto> getCars(List<UUID> carUids);

    @PatchMapping("${services.cars.url.base}/{carId}")
    ResponseEntity<?> changeAvailability(@PathVariable UUID carId, @RequestParam boolean availability);

}