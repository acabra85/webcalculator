package com.acabra.mmind.auth;

public class MMindRequestValidator {
    public static final int SECRET_LENGTH = 4;
    public static void validateSecret(String secret) {
        if(secret == null || secret.length() != SECRET_LENGTH) {
            throw new IllegalArgumentException("Invalid length for given secret expected: " + SECRET_LENGTH);
        }
    }
}
