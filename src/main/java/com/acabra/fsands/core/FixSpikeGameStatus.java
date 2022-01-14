package com.acabra.fsands.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Builder(setterPrefix = "with")
@Jacksonized
@Getter
public class FixSpikeGameStatus {
    private final FixSpikeGameStateType type;
    private final FixSpikeGameStateAction action;
    private final GameOverValues gameOverValues;

    @JsonCreator
    public FixSpikeGameStatus(@JsonProperty("type") FixSpikeGameStateType type,
                           @JsonProperty("action") FixSpikeGameStateAction action,
                           @JsonProperty("gameOverValues") GameOverValues gameOverValues){
        this.type = type;
        this.action = action;
        this.gameOverValues = gameOverValues;
    }

    private FixSpikeGameStatus(FixSpikeGameStateType state, FixSpikeGameStateAction action) {
        this(state, action, null);
    }

    public static FixSpikeGameStatus gameOver(long result, String opponentSecret) {
        GameOverValues val = new GameOverValues(opponentSecret, result);
        return new FixSpikeGameStatus(FixSpikeGameStateType.GAME_OVER_STATE, FixSpikeGameStateAction.NO_ACT, val);
    }

    public static FixSpikeGameStatus notStarted() {
        return new FixSpikeGameStatus(FixSpikeGameStateType.GAME_NOT_STARTED_STATE, FixSpikeGameStateAction.AWAIT_GUEST_ACT);
    }

    public static FixSpikeGameStatus requestRestart() {
        return new FixSpikeGameStatus(FixSpikeGameStateType.GAME_IN_PROGRESS_STATE, FixSpikeGameStateAction.RESTART_ACT);
    }

    public static FixSpikeGameStatus makeMove() {
        return new FixSpikeGameStatus(FixSpikeGameStateType.GAME_IN_PROGRESS_STATE, FixSpikeGameStateAction.MAKE_MOVE_ACT);
    }

    public static FixSpikeGameStatus awaitMove() {
        return new FixSpikeGameStatus(FixSpikeGameStateType.GAME_IN_PROGRESS_STATE, FixSpikeGameStateAction.AWAIT_OPPONENT_ACT);
    }

    @Value
    private static class GameOverValues {
        String opponentSecret;
        long result;
    }
}
