package com.acabra.mmind.auth;

import com.acabra.mmind.request.MMindDeleteTokenRequest;

public class MMindRequestValidator {
    public static final int SECRET_LENGTH = 4;

    public static void validateSecret(String secret) {
        if(secret == null || secret.length() != SECRET_LENGTH) {
            throw new IllegalArgumentException("Invalid length for given secret expected: " + SECRET_LENGTH);
        }
    }

    public static void validateDeleteTokenRequest(MMindDeleteTokenRequest req) {
        if(req.getTokenToDelete().length() != MMindTokenInfo.TOKEN_LEN
                || req.getUserToken().length() != MMindTokenInfo.TOKEN_LEN
        || req.getUserToken().equals(req.getTokenToDelete())) {
            throw new IllegalArgumentException("Invalid Delete Request: " + SECRET_LENGTH);
        }
    }
}
