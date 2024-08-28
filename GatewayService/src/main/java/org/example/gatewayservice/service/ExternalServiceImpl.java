package org.example.gatewayservice.service;

import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gatewayservice.dto.car.CarDto;
import org.example.gatewayservice.dto.car.CarResponseDto;
import org.example.gatewayservice.dto.payment.PaymentResponseDto;
import org.example.gatewayservice.dto.rental.RentalRequestDto;
import org.example.gatewayservice.dto.rental.RentalResponseDto;
import org.example.gatewayservice.exception.InternalServiceException;
import org.example.gatewayservice.feign.CarClient;
import org.example.gatewayservice.feign.PaymentClient;
import org.example.gatewayservice.feign.RentalClient;
import org.example.gatewayservice.wrapper.FallbackWrapper;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExternalServiceImpl implements ExternalService {

    private final ModelMapper modelMapper;
    private final CarClient carClient;
    private final PaymentClient paymentClient;
    private final RentalClient rentalClient;

    @Override
    public Page<CarDto> getCars(boolean showAll, int page, int size) {
        try {
            Page<CarResponseDto> carResponseDtos = carClient.getCars(showAll, page, size);

            log.info("Received {} entities from car service", carResponseDtos.getTotalElements());

            return mapToCarDto(carResponseDtos);
        } catch (FeignException e) {
            log.error("Exception was occurred when getting cars. Exception: {}", e.getMessage());
            throw new InternalServiceException("Unable to get info from Car service.");
        }
    }

    @Override
    public Map<UUID, CarResponseDto> getCars(List<UUID> carUids) {
        try {
            return carClient.getCars(carUids)
                    .stream()
                    .collect(Collectors.toMap(CarResponseDto::carUid, Function.identity()));
        } catch (FeignException e) {
            log.error("Exception was occurred during calling car service. Exception: {}", e.getMessage());

            return buildIdFilledObjects(carUids, this::fillOnlyIdInCarOutDto);
        }
    }

    @Override
    public CarResponseDto getCar(UUID carUid) {
        try {
            return carClient.getCar(carUid);
        } catch (FeignException.NotFound e) {
            log.error("404 during getting car with uuid [{}]. Exception: {}", carUid, e.getMessage());

            throw e;
        } catch (FeignException e) {
            log.error("Exception was occurred during calling car service. Exception: {}", e.getMessage());

            return new CarResponseDto(carUid,
                    null, null, null, 0, null, 0, false);
        }
    }

    @Override
    public boolean changeCarAvailability(UUID carId, boolean availability) {
        try {
            carClient.changeAvailability(carId, availability);

            log.info("Changed car [{}] availability to [{}]", carId, availability);

            return true;
        } catch (FeignException.NotFound e) {
            log.error("Trying to change availability for non existing car {}", carId);
            throw new EntityNotFoundException("There is no car with id = %s".formatted(carId));
        } catch (FeignException e) {
            log.error("Exception was occurred when changing availability of car {}. Exception: {}", carId, e.getMessage());

            return false;
        }
    }

    @Override
    public FallbackWrapper<PaymentResponseDto> createPayment(int totalPrice) {
        try {
            return new FallbackWrapper<>(paymentClient.createPayment(totalPrice), true);
        } catch (FeignException e) {
            log.error("Exception was occurred during creating payment for price [{}]. Exception: {}",
                    totalPrice, e.getMessage());

            return new FallbackWrapper<>(null, false);
        }
    }

    @Override
    public FallbackWrapper<RentalResponseDto> createRental(String username, RentalRequestDto rentalInDto) {
        try {
            return new FallbackWrapper<>(rentalClient.createRental(username, rentalInDto), true);
        } catch (FeignException e) {
            log.error("Exception was occurred during creating rental for user [{}], rental [{}]. Exception: {}",
                    username, rentalInDto, e.getMessage());

            return new FallbackWrapper<>(null, false);
        }
    }

    @Override
    public boolean cancelRental(String username, UUID rentalUid) {
        try {
            rentalClient.cancelRental(rentalUid, username);

            return true;
        } catch (FeignException.NotFound e) {
            log.error("404 during cancelling rental [{}] of user [{}]. Exception: {}", rentalUid, username, e.getMessage());

            throw new EntityNotFoundException(e.getMessage());
        } catch (FeignException e) {
            log.error("Exception was occurred during cancelling rental [{}] of user [{}]. Exception: {}",
                    rentalUid, username, e.getMessage());

            return false;
        }
    }

    @Override
    public List<RentalResponseDto> getRentals(String username) {
        try {
            return rentalClient.getRentals(username).getBody();
        } catch (FeignException.NotFound e) {
            log.error("404 during getting user [{}] rentals. Exception: {}", username, e.getMessage());
            throw e;
        } catch (FeignException e) {
            log.error("Exception was occurred during calling rental service. Exception: {}", e.getMessage());

            throw new InternalServiceException("Unable to get info from Rental service.");
        }
    }

    @Override
    public FallbackWrapper<RentalResponseDto> getRental(String username, UUID rentalUid) {
        try {
            RentalResponseDto rental = rentalClient.getRental(rentalUid, username).getBody();
            log.info("Get rental form rental service: {}", rental);

            return new FallbackWrapper<>(rental, true);
        } catch (FeignException.NotFound e) {
            log.error("404 during getting user [{}] rental with id [{}]. Exception: {}", username, rentalUid, e.getMessage());
            throw new EntityNotFoundException(e.getMessage());
        } catch (FeignException e) {
            log.error("Exception was occurred during calling rental service. Exception: {}", e.getMessage());

            return new FallbackWrapper<>(null, false);
        }
    }

    @Override
    public boolean cancelPayment(UUID paymentsUid) {
        try {
            paymentClient.cancelPayment(paymentsUid);

            return true;
        } catch (FeignException.NotFound e) {
            log.error("404 during cancelling payment with id [{}]. Exception: {}", paymentsUid, e.getMessage());

            throw new EntityNotFoundException(e.getMessage());
        } catch (FeignException e) {
            log.error("Exception was occurred during calling payment service. Exception: {}", e.getMessage());

            return false;
        }
    }

    @Override
    public Map<UUID, PaymentResponseDto> getPayments(List<UUID> paymentsUids) {
        try {
            List<PaymentResponseDto> payments = paymentClient.getPayments(paymentsUids);
            return payments.stream()
                    .collect(Collectors.toMap(PaymentResponseDto::paymentUid, Function.identity()));
        } catch (FeignException e) {
            log.error("Exception was occurred during calling payment service. Exception: {}", e.getMessage());

            return buildIdFilledObjects(paymentsUids, this::fillOnlyIdInPaymentOutDto);
        }
    }

    @Override
    public PaymentResponseDto getPayment(UUID paymentUid) {
        try {
            return paymentClient.getPayment(paymentUid);
        } catch (FeignException.NotFound e) {
            log.error("404 during getting payment with uuid [{}]. Exception: {}", paymentUid, e.getMessage());

            throw e;
        } catch (FeignException e) {
            log.error("Exception was occurred during calling payment service. Exception: {}", e.getMessage());
            return new PaymentResponseDto(paymentUid, null, 0);
        }
    }

    @Override
    public boolean finishRental(UUID rentalUid, String username) {
        try {
            rentalClient.finishRental(rentalUid, username);
            return true;
        } catch (FeignException.NotFound e) {
            log.error("404 during finishing rental with uuid [{}]. Exception: {}", rentalUid, e.getMessage());

            throw new EntityNotFoundException(e.getMessage());
        } catch (FeignException e) {
            log.error("Exception was occurred during calling rental service. Exception: {}", e.getMessage());

            return false;
        }
    }

    private Page<CarDto> mapToCarDto(Page<CarResponseDto> page) {
        return page.map(car -> modelMapper.map(car, CarDto.class));
    }

    private PaymentResponseDto fillOnlyIdInPaymentOutDto(UUID uuid) {
        return new PaymentResponseDto(uuid, null, 0);
    }

    private CarResponseDto fillOnlyIdInCarOutDto(UUID uuid) {
        return new CarResponseDto(uuid,
                null, null, null, 0, null, 0, false);
    }

    private <R> Map<UUID, R> buildIdFilledObjects(List<UUID> carUids, Function<UUID, R> mapperFunction) {
        HashMap<UUID, R> result = new HashMap<>();

        carUids.forEach(uuid ->
                result.put(uuid, mapperFunction.apply(uuid)));

        return result;
    }

}
