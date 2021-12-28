package com.acabra.mmind.response;

import com.acabra.mmind.core.MMindAuthAction;
import lombok.Builder;
import lombok.Getter;

@Builder(setterPrefix = "with")
@Getter
public class MMindAuthResponse {
    Long roomNumber;
    String roomPassword;
    String token;
    MMindAuthAction action;
}
