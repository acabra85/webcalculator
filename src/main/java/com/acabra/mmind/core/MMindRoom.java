package com.acabra.mmind.core;

import com.acabra.mmind.MMindGameManager;
import com.acabra.mmind.request.MMindJoinRoomRequestDTO;
import lombok.Builder;
import lombok.Getter;

@Builder(setterPrefix = "with")
@Getter
public class MMindRoom {
    private final Long roomNumber;
    private final MMindGameManager manager;
    private final String password = MMindPasswords.generateRandomPassword(5);
    private final long expiresAfter;
}
