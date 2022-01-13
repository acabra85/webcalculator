package com.acabra.fsands.auth;

import com.acabra.fsands.request.FixSpikeDeleteRoomRequest;
import com.acabra.fsands.request.FixSpikeDeleteTokenRequest;
import com.acabra.fsands.request.FixSpikeJoinRoomRequestDTO;

public class FixSpikeRequestValidator {
    public static final int SECRET_LENGTH = 4;

    public static void validateSecret(String secret) {
        if(secret == null || secret.length() != SECRET_LENGTH) {
            throw new IllegalArgumentException("Invalid length for given secret expected: " + SECRET_LENGTH);
        }
    }

    public static void validateDeleteTokenRequest(FixSpikeDeleteTokenRequest req) {
        if(isTokenInvalid(req.getUserToken()) || isTokenInvalid(req.getTokenToDelete())
            || req.getUserToken().equals(req.getTokenToDelete())) {
            throw new IllegalArgumentException("Invalid Delete Request: " + SECRET_LENGTH);
        }
    }

    private static boolean isTokenInvalid(String token) {
        return token.length() != FixSpikeTokenInfo.TOKEN_LEN;
    }

    public static void validateJoinRequest(FixSpikeJoinRoomRequestDTO joinRequest) {
        validateSecret(joinRequest.getSecret());
        validateName(joinRequest.getPlayerName());
    }

    private static void validateName(String playerName) {
        if(playerName == null || playerName.isEmpty() || playerName.isBlank()) {
            throw new IllegalArgumentException("Empty player name");
        }
    }

    public static void validateDeleteRoomRequest(FixSpikeDeleteRoomRequest req) {
        if(isTokenInvalid(req.getToken())) {
            throw new IllegalArgumentException("Invalid token");
        }

    }
}
