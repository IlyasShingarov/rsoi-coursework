package org.example.carservice.service;

import org.example.carservice.dto.CarResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public interface CarService {
    Page<CarResponseDto> getCars(boolean showAll, Pageable pageable);

    void changeAvailability(UUID carId, boolean availability);

    List<CarResponseDto> getCars(Set<UUID> carUids);

    CarResponseDto getCar(UUID carId);
}