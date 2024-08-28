package org.example.paymentservice.service;

import org.example.paymentservice.dto.PaymentResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface PaymentService {
    PaymentResponseDto createPayment(int price);

    List<PaymentResponseDto> getPayments(List<UUID> paymentsUids);

    void cancelPayment(UUID paymentUid);

    Optional<PaymentResponseDto> getPayment(UUID paymentUid);
}
