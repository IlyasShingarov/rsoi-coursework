package org.example.gatewayservice.feign;

import org.example.gatewayservice.dto.payment.PaymentResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "payment", url = "${feign.payment.url}")
public interface PaymentClient {
    @GetMapping(path = "${services.payment.url.base}")
    List<PaymentResponseDto> getPayments(@RequestParam List<UUID> paymentUids);

    @GetMapping(path = "${services.payment.url.base}/{paymentUid}")
    PaymentResponseDto getPayment(@PathVariable UUID paymentUid);

    @PostMapping(path = "${services.payment.url.base}")
    PaymentResponseDto createPayment(@RequestBody int price);

    @DeleteMapping(path = "${services.payment.url.base}/{paymentUid}")
    void cancelPayment(@PathVariable UUID paymentUid);
}
