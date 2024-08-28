package org.example.carservice;

import org.example.carservice.domain.CarType;
import org.example.carservice.entity.Car;
import org.example.carservice.repository.CarRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class CarServiceApplication implements CommandLineRunner {

    public CarServiceApplication(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(CarServiceApplication.class, args);
    }

    private final CarRepository carRepository;

    @Override
    public void run(String... args) throws Exception {
        var car = new Car();
        car.setId(1);
        car.setCarUid(UUID.fromString("109b42f3-198d-4c89-9276-a7520a7120ab"));
        car.setBrand("Mercedes Benz");
        car.setModel("GLA 250");
        car.setRegistrationNumber("ЛО777Х799");
        car.setPower(249);
        car.setType(CarType.SEDAN);
        car.setPrice(3500);
        car.setAvailability(true);
        carRepository.save(car);
    }

}
