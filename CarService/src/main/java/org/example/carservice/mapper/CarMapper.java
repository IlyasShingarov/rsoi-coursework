package org.example.carservice.mapper;

import org.example.carservice.dto.CarResponseDto;
import org.example.carservice.entity.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarMapper {

    @Mapping(target = "available", source = "availability")
    CarResponseDto toResponse(Car entity);

}