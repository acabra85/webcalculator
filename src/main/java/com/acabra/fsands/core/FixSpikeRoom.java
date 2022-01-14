package com.acabra.fsands.core;

import com.acabra.fsands.FixSpikeGameManager;
import lombok.Builder;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Builder(setterPrefix = "with")
@Getter
public class FixSpikeRoom {
    private final Long roomNumber;
    private final FixSpikeGameManager manager;
    private final String password;
    private final long expiresAfter;

    public FixSpikeRoom(Long roomNumber, FixSpikeGameManager manager, String password, long expiresAfter) {
        this.roomNumber = roomNumber;
        this.manager = manager;
        this.password = password == null || password.length() != FixSpikePasswords.ROOM_PWD_LEN ?
                FixSpikePasswords.generateRandomPassword(FixSpikePasswords.ROOM_PWD_LEN) : password;
        this.expiresAfter = expiresAfter;
    }

    public FixSpikeRoom restartGame(FixSpikeGameManager newManager) {
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
        if(null == token) return false;
        final String hostToken = manager.retrieveHostToken();
        final String guestToken = manager.retrieveGuestToken();
        return token.equals(hostToken) || token.equals(guestToken);
    }

}
