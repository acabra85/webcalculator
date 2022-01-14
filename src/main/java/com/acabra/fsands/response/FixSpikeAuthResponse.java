package com.acabra.fsands.response;

import com.acabra.fsands.core.FixSpikeAuthAction;
import lombok.Builder;
import lombok.Getter;

@Builder(setterPrefix = "with")
@Getter
public class FixSpikeAuthResponse {
    Long roomNumber;
    Long playerId;
    String roomPassword;
    String hostName;
    String token;
    FixSpikeAuthAction action;
}
