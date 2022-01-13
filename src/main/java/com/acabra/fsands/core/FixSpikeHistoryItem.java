package com.acabra.fsands.core;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder(setterPrefix = "with")
@Getter
public class FixSpikeHistoryItem {
    @NonNull String playerToken;
    @NonNull FixSpikeMoveResult moveResult;
    @NonNull Long playerId;
}
