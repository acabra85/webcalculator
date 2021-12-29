package com.acabra.mmind.core;

import com.acabra.mmind.MMindGameManager;
import lombok.Builder;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Builder(setterPrefix = "with")
@Getter
public class MMindRoom {
    private final Long roomNumber;
    private final MMindGameManager manager;
    private final String password;
    private final long expiresAfter;

    public MMindRoom(Long roomNumber, MMindGameManager manager, String password, long expiresAfter) {
        this.roomNumber = roomNumber;
        this.manager = manager;
        this.password = password == null || password.length() != MMindPasswords.ROOM_PWD_LEN ?
                MMindPasswords.generateRandomPassword(MMindPasswords.ROOM_PWD_LEN) : password;
        this.expiresAfter = expiresAfter;
    }

    public MMindRoom restartGame(MMindGameManager newManager) {
        return builder()
                .withRoomNumber(roomNumber)
                .withManager(newManager)
                .withPassword(password)
                .withExpiresAfter(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30))
                .build();
    }

    public String getHostName() {
        return manager.getHostName();
    }

    public String getGuestName() {
        return manager.getGuestName();
    }

    public boolean hasPlayerWithToken(String token) {
        final String hostToken = manager.retrieveHostToken();
        final String guestToken = manager.retrieveGuestToken();
        return token.equals(hostToken) || token.equals(guestToken);
    }
}
