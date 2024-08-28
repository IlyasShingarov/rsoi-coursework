package org.example.carservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.carservice.dto.CarResponseDto;
import org.example.carservice.service.CarService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("${api.url.base}")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping
    Page<CarResponseDto> getCars(@RequestParam(defaultValue = "false") boolean showAll,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "100") int size) {
        if (page == 0) page = 1;
        page = page - 1;
        PageRequest pageRequest = PageRequest.of(page, size);
        log.debug("Request for reading cars. showAll = {}. page request = {}", showAll, pageRequest);
        return carService.getCars(showAll, pageRequest);
    }

    @GetMapping("/{carId}")
    CarResponseDto getCar(@PathVariable UUID carId) {
        return carService.getCar(carId);
    }

    @PostMapping
    List<CarResponseDto> getCars(@RequestBody Set<UUID> carUids) {
        return carService.getCars(carUids);
    }

    @PatchMapping("/{carId}")
    ResponseEntity<?> changeAvailability(@PathVariable UUID carId, @RequestParam boolean availability) {
        carService.changeAvailability(carId, availability);
        return ResponseEntity.ok().build();
    }
}
