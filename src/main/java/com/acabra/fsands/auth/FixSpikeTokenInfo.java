package com.acabra.fsands.auth;

import lombok.Builder;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Builder(setterPrefix = "with")
@Getter
public class FixSpikeTokenInfo {
    public static final long EXTENDED_PERIOD_MINUTES = TimeUnit.MINUTES.toMillis(30);
    public static final int TOKEN_LEN = 36;

    final private String token;
    final private long expiresAfter;
    final private long roomNumber;
    final private boolean adminToken;

    public FixSpikeTokenInfo renew() {
        return builder()
                .withToken(token)
                .withExpiresAfter(System.currentTimeMillis() + EXTENDED_PERIOD_MINUTES)
                .withRoomNumber(roomNumber)
                .withAdminToken(adminToken)
                .build();
    }
}
