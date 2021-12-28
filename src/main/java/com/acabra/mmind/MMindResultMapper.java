package com.acabra.mmind;

import com.acabra.mmind.auth.MMindTokenInfo;
import com.acabra.mmind.core.MMindRoom;
import com.acabra.mmind.core.MMmindMoveResult;
import com.acabra.mmind.response.MMindMoveResultDTO;
import com.acabra.mmind.response.MMindSystemStatusResponse;
import com.acabra.mmind.response.MMindSystemStatusRoomDTO;
import com.acabra.mmind.response.MMindTokenInfoDTO;
import com.acabra.mmind.utils.TimeDateHelper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MMindResultMapper {
    public static MMindMoveResultDTO toResultDTO(MMmindMoveResult moveResult) {
        if(null == moveResult) {
            return null;
        }
        return MMindMoveResultDTO.builder()
                .withId(moveResult.getId())
                .withIndex(moveResult.getIndex())
                .withFixes(moveResult.getFixes())
                .withSpikes(moveResult.getSpikes())
                .withGuess(moveResult.getGuess())
                .withPlayerName(moveResult.getPlayerName())
                .build();
    }

    public static MMindSystemStatusResponse toSystemStatusResponse(long id, List<MMindRoom> rooms,
                                                                   Map<String, MMindTokenInfo> tokens) {
        return MMindSystemStatusResponse.builder()
                .withId(id)
                .withFailure(false)
                .withRooms(toSystemRoomsDTO(rooms, tokens))
                .build();
    }

    private static List<MMindSystemStatusRoomDTO> toSystemRoomsDTO(List<MMindRoom> rooms, Map<String, MMindTokenInfo> tokens) {
        return rooms.stream()
                .map(room -> toMMindSystemRoomDTO(tokens, room))
                .collect(Collectors.toList());
    }

    private static MMindSystemStatusRoomDTO toMMindSystemRoomDTO(Map<String, MMindTokenInfo> tokens, MMindRoom room) {
        final String hostToken = room.getManager().retrieveHostToken();
        final String guestToken = room.getManager().retrieveGuestToken();
        return MMindSystemStatusRoomDTO.builder()
                .withHostToken(
                    MMindTokenInfoDTO.builder()
                        .withToken(hostToken)
                        .withExpiresAfter(TimeDateHelper.asStringFromEpoch(tokens.get(hostToken).getExpiresAfter()))
                        .build()
                )
                .withGuestToken(
                    MMindTokenInfoDTO.builder()
                        .withToken(guestToken)
                        .withExpiresAfter(TimeDateHelper.asStringFromEpoch(tokens.get(guestToken).getExpiresAfter()))
                        .build()
                )
                .withExpiresAfter(TimeDateHelper.asStringFromEpoch(room.getExpiresAfter()))
                .withNumber(room.getRoomNumber())
                .build();
    }
}
