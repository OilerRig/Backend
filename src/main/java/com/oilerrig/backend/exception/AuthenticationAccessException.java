package com.oilerrig.backend.exception;

public class AuthenticationAccessException extends RuntimeException {
    public AuthenticationAccessException(String message) {
        super(message);
    }
}
