package com.acabra.mmind.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder(setterPrefix = "with")
@Getter
public class MMindMoveResultDTO {
    private final int fixes;
    private final int spikes;
    private final char[] guess;

    @JsonCreator
    public MMindMoveResultDTO(@JsonProperty("fixes") int fixes, @JsonProperty("spikes") int spikes,
                              @JsonProperty("guess") char[] guess) {
        this.fixes = fixes;
        this.spikes = spikes;
        this.guess = guess;
    }
}
