package com.acabra.fsands;

import com.acabra.fsands.auth.FixSpikeTokenInfo;
import com.acabra.fsands.core.FixSpikeRoom;
import com.acabra.fsands.core.FixSpikeMoveResult;
import com.acabra.fsands.response.*;
import com.acabra.fsands.utils.B64Helper;
import com.acabra.fsands.utils.TimeDateHelper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FixSpikeResultMapper {
    public static FixSpikeMoveResultDTO toMoveResultDTO(FixSpikeMoveResult moveResult) {
        if(null == moveResult) {
            return null;
        }
        return FixSpikeMoveResultDTO.builder()
                .withId(moveResult.getId())
                .withIndex(moveResult.getIndex())
                .withFixes(moveResult.getFixes())
                .withSpikes(moveResult.getSpikes())
                .withGuess(moveResult.getGuess())
                .withPlayerName(moveResult.getPlayerName())
                .build();
    }

    public static FixSpikeSystemStatusResponse toSystemStatusResponse(long id, List<FixSpikeRoom> rooms,
                                                                   Map<String, FixSpikeTokenInfo> tokens) {
        long now = System.currentTimeMillis();
        return FixSpikeSystemStatusResponse.builder()
                .withId(id)
                .withFailure(false)
                .withRooms(toSystemRoomsDTO(rooms, tokens, now))
                .withTokens(toSystemTokensDTO(tokens, now))
                .build();
    }

    private static List<FixSpikeTokenInfoDTO> toSystemTokensDTO(Map<String, FixSpikeTokenInfo> tokens, long now) {
        return tokens.values().stream()
                .map(ti -> FixSpikeResultMapper.toTokenDTO(ti, now))
                .collect(Collectors.toList());
    }

    private static List<FixSpikeSystemStatusRoomDTO> toSystemRoomsDTO(List<FixSpikeRoom> rooms, Map<String, FixSpikeTokenInfo> tokens, long now) {
        return rooms.stream()
                .map(room -> toFixSpikeSystemRoomDTO(tokens, room, now))
                .collect(Collectors.toList());
    }

    private static FixSpikeSystemStatusRoomDTO toFixSpikeSystemRoomDTO(Map<String, FixSpikeTokenInfo> tokens, FixSpikeRoom room, long now) {
        final String hostToken = room.getManager().retrieveHostToken();
        final String guestToken = (room.getManager().hostWaitingForGuest()) ? null : room.getManager().retrieveGuestToken();
        return FixSpikeSystemStatusRoomDTO.builder()
                .withHostToken(hostToken == null ? null : toTokenDTO(tokens.get(hostToken), now))
                .withGuestToken(guestToken == null ? null : toTokenDTO(tokens.get(guestToken), now))
                .withExpiresAfter(TimeDateHelper.asStringFromEpoch(room.getExpiresAfter()))
                .withExpired(now > room.getExpiresAfter())
                .withNumber(room.getRoomNumber())
                .build();
    }

    private static FixSpikeTokenInfoDTO toTokenDTO(FixSpikeTokenInfo tokenInfo, long now) {
        return FixSpikeTokenInfoDTO.builder()
                .withToken(tokenInfo.getToken())
                .withExpiresAfter(TimeDateHelper.asStringFromEpoch(tokenInfo.getExpiresAfter()))
                .withRoomNumber(tokenInfo.getRoomNumber())
                .withIsAdmin(tokenInfo.isAdminToken())
                .withExpired(now > tokenInfo.getExpiresAfter())
                .build();
    }

    public static FixSpikeJoinRoomResponse toJoinResponse(long id, FixSpikeAuthResponse authResponse, String playerName) {
        return FixSpikeJoinRoomResponse.builder()
                .withId(id)
                .withFailure(false)
                .withPlayerId(authResponse.getPlayerId())
                .withHostName(authResponse.getHostName())
                .withToken(authResponse.getToken())
                .withAction(authResponse.getAction().toString())
                .withRoomPassword(B64Helper.encode(authResponse.getRoomPassword()))
                .withRoomNumber(authResponse.getRoomNumber())
                .withUserName(playerName)
                .build();
    }
}
