package org.example.paymentservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.paymentservice.dto.PaymentResponseDto;
import org.example.paymentservice.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("${api.url.base}")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    ResponseEntity<?> getPayments(@RequestParam List<UUID> paymentUids) {
        log.info("Request for reading payments with uuids {}", paymentUids);

        return ResponseEntity.ok(paymentService.getPayments(paymentUids));
    }

    @GetMapping("/{paymentUid}")
    ResponseEntity<?> getPayment(@PathVariable UUID paymentUid) {
        log.info("Request for reading payment {}", paymentUid);

        Optional<PaymentResponseDto> payment = paymentService.getPayment(paymentUid);

        return payment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    ResponseEntity<PaymentResponseDto> createPayment(@RequestBody int price) {
        log.info("Request for creating payment with price {}", price);

        return ResponseEntity.ok(paymentService.createPayment(price));
    }

    @DeleteMapping("/{paymentUid}")
    ResponseEntity<?> cancelPayment(@PathVariable UUID paymentUid) {
        log.info("Request for cancelling payment {}", paymentUid);

        paymentService.cancelPayment(paymentUid);
        return ResponseEntity.noContent().build();
    }

}