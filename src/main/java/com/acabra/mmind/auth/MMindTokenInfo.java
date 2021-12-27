package com.acabra.mmind.auth;

import lombok.Builder;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Builder(setterPrefix = "with")
@Getter
public class MMindTokenInfo {
    public static final long EXTENDED_PERIOD_MINUTES = TimeUnit.MINUTES.toMillis(30);
    public static final int TOKEN_LEN = 36;
    final private String token;
    final private long expiresAfter;
    final private long roomNumber;
    private boolean adminToken;

    public MMindTokenInfo renew() {
        return builder()
                .withToken(token)
                .withRoomNumber(roomNumber)
                .withExpiresAfter(System.currentTimeMillis() + EXTENDED_PERIOD_MINUTES)
                .build();
    }
}
