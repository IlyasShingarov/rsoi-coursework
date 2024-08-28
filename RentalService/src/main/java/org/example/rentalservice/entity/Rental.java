package org.example.rentalservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.rentalservice.constants.RentalStatus;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "rental")
@Table(name = "rental")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    UUID rentalUid;
    String username;
    UUID paymentUid;
    UUID carUid;
    LocalDate dateFrom;
    LocalDate dateTo;

    @Enumerated(EnumType.STRING)
    RentalStatus status;
}