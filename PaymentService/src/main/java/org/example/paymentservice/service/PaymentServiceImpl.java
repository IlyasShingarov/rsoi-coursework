package org.example.paymentservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.paymentservice.constants.PaymentStatus;
import org.example.paymentservice.dto.PaymentResponseDto;
import org.example.paymentservice.entity.Payment;
import org.example.paymentservice.mapper.PaymentMapper;
import org.example.paymentservice.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public PaymentResponseDto createPayment(int price) {
        Payment payment = new Payment(null, UUID.randomUUID(), PaymentStatus.PAID, price);

        Payment savedPayment = paymentRepository.save(payment);

        log.info("Payment was created: {}", savedPayment);

        return paymentMapper.toResponse(savedPayment);
    }

    @Override
    public List<PaymentResponseDto> getPayments(List<UUID> paymentsUids) {
        List<Payment> payments = paymentRepository.findByPaymentUidIn(paymentsUids);

        return payments.stream()
                .map(paymentMapper::toResponse)
                .toList();
    }

    @Override
    public Optional<PaymentResponseDto> getPayment(UUID paymentUid) {
        Optional<Payment> payment = paymentRepository.findByPaymentUid(paymentUid);

        return payment.map(paymentMapper::toResponse);
    }

    @Override
    public void cancelPayment(UUID paymentUid) {
        Payment payment = paymentRepository.findByPaymentUid(paymentUid).orElseThrow(() -> {
            log.error("Deletion was aborted. There is no payment with id = {}", paymentUid);

            return new EntityNotFoundException("Unable to cancel payment " + paymentUid);
        });

        payment.setStatus(PaymentStatus.CANCELED);
        paymentRepository.save(payment);

        log.info("Payment {} status was updated to {}", paymentUid, payment.getStatus());
    }

}