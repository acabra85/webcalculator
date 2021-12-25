package com.acabra.mmind.core;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(setterPrefix = "with")
public class MMmindMoveResult {
    @NonNull
    Integer fixes;
    @NonNull
    Integer spikes;
    @NonNull
    char[] guess;
}
