package com.oilerrig.backend.exception;

public class ValidityException extends RuntimeException {

    public ValidityException(String target) {
        super(target);
    }
}
