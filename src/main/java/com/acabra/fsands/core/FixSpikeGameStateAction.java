package com.acabra.fsands.core;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum FixSpikeGameStateAction {
    @JsonProperty("AWAIT_GUEST_ACT") AWAIT_GUEST_ACT,
    @JsonProperty("MAKE_MOVE_ACT") MAKE_MOVE_ACT,
    @JsonProperty("NO_ACT") NO_ACT,
    @JsonProperty("RESTART_ACT") RESTART_ACT,
    @JsonProperty("AWAIT_OPPONENT_ACT") AWAIT_OPPONENT_ACT,
}
