package org.example.rentalservice.repository;

import org.example.rentalservice.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RentalRepository extends JpaRepository<Rental, Integer> {
    List<Rental> findAllByUsername(String username);

    Optional<Rental> findByUsernameAndRentalUid(String username, UUID rentalUid);
}