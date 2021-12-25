package com.acabra.mmind;

import com.acabra.mmind.core.MMmindMoveResult;
import com.acabra.mmind.response.MMindMoveResultDTO;

public class MMindResultMapper {
    public static MMindMoveResultDTO toResultDTO(MMmindMoveResult moveResult) {
        if(null == moveResult) {
            return null;
        }
        return MMindMoveResultDTO.builder()
                .withIndex(moveResult.getIndex())
                .withFixes(moveResult.getFixes())
                .withSpikes(moveResult.getSpikes())
                .withGuess(moveResult.getGuess())
                .withPlayerName(moveResult.getPlayerName())
                .build();
    }
}
