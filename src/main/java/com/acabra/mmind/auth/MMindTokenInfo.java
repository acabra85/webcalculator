package com.acabra.mmind.auth;

import lombok.Builder;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Builder(setterPrefix = "with")
@Getter
public class MMindTokenInfo {
    public static final long EXTENDED_PERIOD_MINUTES = TimeUnit.MINUTES.convert(30, TimeUnit.MILLISECONDS);
    final private String token;
    final private long expiresAfter;
    final private long roomNumber;

    public MMindTokenInfo renew() {
        return builder()
                .withToken(token)
                .withRoomNumber(roomNumber)
                .withExpiresAfter(System.currentTimeMillis() + EXTENDED_PERIOD_MINUTES)
                .build();
    }
}
