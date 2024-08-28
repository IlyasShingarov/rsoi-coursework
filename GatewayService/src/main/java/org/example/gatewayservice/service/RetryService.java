package org.example.gatewayservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gatewayservice.availability.AvailabilityService;
import org.example.gatewayservice.wrapper.RetryWrapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class RetryService {
    public static final int SLEEP_TIME = 2000;
    public static final int CALL_TIMEOUT = 1000;

    private final AvailabilityService availabilityService;
    private final Queue<RetryWrapper> requests = new LinkedBlockingDeque<>();
    private final Set<Integer> currentRequestHashes = new HashSet<>();

    public void addRequest(String serviceName, Integer hash, Supplier<Boolean> runnable) {
        int fullHash = buildFullHash(hash, runnable);

        if (!currentRequestHashes.contains(fullHash)) {
            currentRequestHashes.add(fullHash);
            RetryWrapper request = new RetryWrapper(runnable, fullHash, serviceName);

            requests.add(request);
            log.debug("Added new request {}", request.getFullHash());
        }
    }

    private int buildFullHash(Integer hash, Supplier<Boolean> runnable) {
        return (hash.toString() + runnable.hashCode()).hashCode();
    }

    @Async
    public void resendRequests() throws InterruptedException {
        log.debug("Start resending requests.");

        while (true) {
            RetryWrapper request = requests.peek();

            if (request != null) {
                processRequest(request);
            } else {
                Thread.sleep(SLEEP_TIME);
            }
        }
    }

    private void processRequest(RetryWrapper request) {
        LocalDateTime lastCall = request.getLastCall();
        LocalDateTime currentTime = LocalDateTime.now();

        requests.poll();

        if (!isTimeout(request, currentTime)) {
            boolean serviceAvailable = availabilityService.checkAvailability(request.getServiceName());
            if (serviceAvailable && lastCall == null || isNeedCall(lastCall, currentTime)) {
                boolean isValidRequest = request.getRunnable().get();
                request.setLastCall(currentTime);

                if (!isValidRequest) {
                    requests.add(request);

                    log.debug("Request {} should be reprocessed.", request.getFullHash());
                } else {
                    log.debug("Request {} was processed", request.getFullHash());
                    currentRequestHashes.remove(request.getFullHash());
                }
            } else {
                requests.add(request);
            }
        } else {
            log.debug("Request {} was deleted from resending queue due timeout", request.getFullHash());
        }
    }

    private boolean isTimeout(RetryWrapper request, LocalDateTime currentTime) {
        return currentTime.isAfter(request.getTimoutTimestamp());
    }

    private boolean isNeedCall(LocalDateTime lastCall, LocalDateTime currentTime) {
        return ChronoUnit.NANOS.between(lastCall, currentTime) > CALL_TIMEOUT;
    }
}