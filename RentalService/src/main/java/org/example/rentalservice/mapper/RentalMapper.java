package org.example.rentalservice.mapper;

import org.example.rentalservice.dto.RentalResponseDto;
import org.example.rentalservice.entity.Rental;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RentalMapper {

    RentalResponseDto toResponse(Rental entity);

}