package org.example.gatewayservice.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FallbackWrapper<T> {
    private T value;
    private boolean isValidResponse;
}