package org.example.gatewayservice.config;


import org.example.gatewayservice.dto.rental.RentalCreateDto;
import org.example.gatewayservice.dto.rental.RentalResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        setUpMapper(mapper);

        TypeMap<RentalResponseDto, RentalCreateDto> rentalTypeMap =
                mapper.createTypeMap(RentalResponseDto.class, RentalCreateDto.class);
        rentalTypeMap.addMapping(RentalResponseDto::getStatus, RentalCreateDto::setStatus);

        return mapper;
    }

    private void setUpMapper(ModelMapper mapper) {
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
    }

}