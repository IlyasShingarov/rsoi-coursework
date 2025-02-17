package org.example.gatewayservice.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class InvalidOperationException extends RuntimeException{
    public InvalidOperationException(String message) {
        super(message);
    }
}
