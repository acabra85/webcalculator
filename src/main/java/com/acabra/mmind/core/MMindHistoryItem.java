package com.acabra.mmind.core;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder(setterPrefix = "with")
@Getter
public class MMindHistoryItem {
    @NonNull String playerToken;
    @NonNull MMindMoveResult moveResult;
    @NonNull Long playerId;
}
