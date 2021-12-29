package com.acabra.mmind.response;

import com.acabra.mmind.core.MMindAuthAction;
import lombok.Builder;
import lombok.Getter;

@Builder(setterPrefix = "with")
@Getter
public class MMindAuthResponse {
    Long roomNumber;
    Long playerId;
    String roomPassword;
    String opponentName;
    String token;
    MMindAuthAction action;
}
