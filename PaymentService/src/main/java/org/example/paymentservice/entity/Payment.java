package org.example.paymentservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.paymentservice.constants.PaymentStatus;

import java.util.UUID;

@Entity(name = "payment")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private UUID paymentUid;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private int price;
}