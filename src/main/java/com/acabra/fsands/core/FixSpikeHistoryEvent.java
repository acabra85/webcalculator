package com.acabra.fsands.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder(setterPrefix = "with")
@Getter
@AllArgsConstructor
public class FixSpikeHistoryEvent {
    @NonNull String playerToken;
    FixSpikeMoveResult moveResult;
    @NonNull Long playerId;
    @NonNull FixSpikeMoveEventType eventType;

    public static FixSpikeHistoryEvent playerLeft(FixSpikePlayer quitter) {
        return new FixSpikeHistoryEvent(quitter.getToken(), null, quitter.getId(), FixSpikeMoveEventType.EXIT);
    }

    public static FixSpikeHistoryEvent playerMove(FixSpikePlayer player, FixSpikeMoveResult moveResult) {
        return new FixSpikeHistoryEvent(player.getToken(), moveResult, player.getId(), FixSpikeMoveEventType.MOVE);
    }

    public static FixSpikeHistoryEvent playerJoin(FixSpikePlayer player) {
        return new FixSpikeHistoryEvent(player.getToken(), null, player.getId(), FixSpikeMoveEventType.JOIN);
    }
}
