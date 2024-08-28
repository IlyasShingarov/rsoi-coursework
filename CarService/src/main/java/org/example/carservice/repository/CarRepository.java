package org.example.carservice.repository;

import jakarta.validation.constraints.NotNull;
import org.example.carservice.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
    Page<Car> findAllByAvailabilityIsTrue(Pageable pageable);

    Optional<Car> findByCarUid(@NotNull UUID carUid);
    List<Car> findAllByCarUidIn(Set<@NotNull UUID> carUid);
}