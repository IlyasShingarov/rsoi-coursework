package org.example.paymentservice.repository;

import org.example.paymentservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByPaymentUid(UUID paymentUid);
    List<Payment> findByPaymentUidIn(List<UUID> paymentUids);
}
