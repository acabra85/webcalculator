package com.acabra.mmind;

import com.acabra.mmind.auth.MMindTokenInfo;
import com.acabra.mmind.core.MMindRoom;
import com.acabra.mmind.core.MMindMoveResult;
import com.acabra.mmind.response.*;
import com.acabra.mmind.utils.B64Helper;
import com.acabra.mmind.utils.TimeDateHelper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MMindResultMapper {
    public static MMindMoveResultDTO toResultDTO(MMindMoveResult moveResult) {
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
        long now = System.currentTimeMillis();
        return MMindSystemStatusResponse.builder()
                .withId(id)
                .withFailure(false)
                .withRooms(toSystemRoomsDTO(rooms, tokens, now))
                .withTokens(toSystemTokensDTO(tokens, now))
                .build();
    }

    private static List<MMindTokenInfoDTO> toSystemTokensDTO(Map<String, MMindTokenInfo> tokens, long now) {
        return tokens.values().stream()
                .map(ti -> MMindResultMapper.toTokenDTO(ti, now))
                .collect(Collectors.toList());
    }

    private static List<MMindSystemStatusRoomDTO> toSystemRoomsDTO(List<MMindRoom> rooms, Map<String, MMindTokenInfo> tokens, long now) {
        return rooms.stream()
                .map(room -> toMMindSystemRoomDTO(tokens, room, now))
                .collect(Collectors.toList());
    }

    private static MMindSystemStatusRoomDTO toMMindSystemRoomDTO(Map<String, MMindTokenInfo> tokens, MMindRoom room, long now) {
        final String hostToken = room.getManager().retrieveHostToken();
        final String guestToken = (room.getManager().awaitingGuest()) ? null : room.getManager().retrieveGuestToken();
        return MMindSystemStatusRoomDTO.builder()
                .withHostToken(hostToken == null ? null : toTokenDTO(tokens.get(hostToken), now))
                .withGuestToken(guestToken == null ? null : toTokenDTO(tokens.get(guestToken), now))
                .withExpiresAfter(TimeDateHelper.asStringFromEpoch(room.getExpiresAfter()))
                .withExpired(now > room.getExpiresAfter())
                .withNumber(room.getRoomNumber())
                .build();
    }

    private static MMindTokenInfoDTO toTokenDTO(MMindTokenInfo tokenInfo, long now) {
        return MMindTokenInfoDTO.builder()
                .withToken(tokenInfo.getToken())
                .withExpiresAfter(TimeDateHelper.asStringFromEpoch(tokenInfo.getExpiresAfter()))
                .withRoomNumber(tokenInfo.getRoomNumber())
                .withIsAdmin(tokenInfo.isAdminToken())
                .withExpired(now > tokenInfo.getExpiresAfter())
                .build();
    }

    public static MMindJoinRoomResponse toJoinResponse(long id, MMindAuthResponse authResponse, String playerName) {
        return MMindJoinRoomResponse.builder()
                .withId(id)
                .withFailure(false)
                .withPlayerId(authResponse.getPlayerId())
                .withOpponentName(authResponse.getOpponentName())
                .withToken(authResponse.getToken())
                .withAction(authResponse.getAction().toString())
                .withRoomPassword(B64Helper.encode(authResponse.getRoomPassword()))
                .withRoomNumber(authResponse.getRoomNumber())
                .withUserName(playerName)
                .build();
    }
}
