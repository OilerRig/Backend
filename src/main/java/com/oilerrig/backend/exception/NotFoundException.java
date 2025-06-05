package com.oilerrig.backend.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String target) {
        super("Could not find " + target);
    }
}
