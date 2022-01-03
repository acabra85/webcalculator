package com.acabra.mmind;

import com.acabra.calculator.response.SimpleResponse;
import com.acabra.mmind.auth.ConstantTimePasswordChecker;
import com.acabra.mmind.auth.MMindTokenInfo;
import com.acabra.mmind.core.*;
import com.acabra.mmind.request.*;
import com.acabra.mmind.response.*;
import com.acabra.mmind.utils.B64Helper;
import com.acabra.mmind.utils.TimeDateHelper;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
public class MMindRoomsAdministrator {
    private static final String ADM_PWD = System.getenv("admpwd");
    private final Map<Long, MMindRoom> rooms;
    private final Map<String, MMindTokenInfo> auth;
    private final AtomicLong playerIdGen;


    private MMindRoomsAdministrator() {
        this.rooms = new HashMap<>();
        this.auth = new HashMap<>();
        this.playerIdGen = new AtomicLong();
    }

    private MMindAuthResponse joinRoomAsGuest(MMindJoinRoomRequestDTO request, MMindRoom room, boolean isAdmin) {
        String token = UUID.randomUUID().toString();
        MMindPlayer player = new MMindPlayer(playerIdGen.getAndIncrement(), request.getPlayerName(), request.getSecret(), token);
        auth.put(token, MMindTokenInfo.builder()
                        .withAdminToken(isAdmin)
                        .withRoomNumber(room.getRoomNumber())
                        .withToken(token)
                        .withExpiresAfter(newTokenExpiration())
                .build());
        room.getManager().addGuest(player);
        return MMindAuthResponse.builder()
                .withToken(token)
                .withPlayerId(player.getId())
                .withOpponentName(room.getHostName())
                .withRoomPassword(room.getPassword())
                .withRoomNumber(room.getRoomNumber())
                .withAction(isAdmin ? MMindAuthAction.JOIN_ADMIN : MMindAuthAction.JOIN_GUEST)
                .build();
    }

    private long newTokenExpiration() {
        return System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30);
    }

    private MMindAuthResponse hostARoom(MMindJoinRoomRequestDTO joinRoomRequest, long roomNumber, boolean isAdmin) {
        String token = UUID.randomUUID().toString();
        long expiresAfter = newTokenExpiration();
        logger.info("--------------->>> Created room {} expires after:{}" , roomNumber, TimeDateHelper.fromEpoch(expiresAfter));
        auth.put(token, MMindTokenInfo.builder()
                .withAdminToken(isAdmin)
                .withRoomNumber(roomNumber)
                .withToken(token)
                .withExpiresAfter(expiresAfter)
                .build());
        MMindPlayer player = new MMindPlayer(playerIdGen.getAndIncrement(),
                joinRoomRequest.getPlayerName(), joinRoomRequest.getSecret(), token);
        MMindRoom room = MMindRoom.builder()
                .withRoomNumber(roomNumber)
                .withManager(MMindGameManager.newGame(player))
                .withExpiresAfter(expiresAfter)
                .build();
        rooms.put(room.getRoomNumber(), room);
        return MMindAuthResponse.builder()
                .withToken(token)
                .withPlayerId(player.getId())
                .withRoomPassword(room.getPassword())
                .withRoomNumber(room.getRoomNumber())
                .withAction(isAdmin ? MMindAuthAction.JOIN_ADMIN : MMindAuthAction.JOIN_HOST)
                .build();
    }

    private boolean isAdminPassword(String password) {
        return ConstantTimePasswordChecker.check(ADM_PWD, password);
    }

    private boolean isAdminToken(String token) {
        MMindTokenInfo tokenInfo = auth.get(token);
        return tokenInfo != null && tokenInfo.isAdminToken();
    }

    public static MMindRoomsAdministrator of() {
        return new MMindRoomsAdministrator();
    }

    private synchronized MMindAuthResponse authenticate(MMindJoinRoomRequestDTO request) {
        MMindRoom room = rooms.get(request.getRoomNumber());
        String password = B64Helper.decode(request.getPassword());
        boolean isAdmin = isAdminPassword(password);
        if(null == room) {
            return hostARoom(request, request.getRoomNumber(), isAdmin);
        }
        if(room.getManager().awaitingGuest()) {
            if((isAdmin || ConstantTimePasswordChecker.check(room.getPassword(), password))) {
                if(!room.hasPlayerWithToken(request.getToken())) {
                    return joinRoomAsGuest(request, room, isAdmin);
                }
            }
            throw new UnsupportedOperationException("Unable to join, wrong password for room: " + request.getRoomNumber());
        }
        throw new UnsupportedOperationException("Unable to join, room is full: " + request.getRoomNumber());
    }

    public synchronized MMindGameManager findRoomManager(MMindRequestDTO req) {
        MMindTokenInfo tokenInfo = auth.get(req.getToken());
        if(tokenInfo == null || tokenInfo.getRoomNumber() != req.getRoomNumber()) {
            throw new UnsupportedOperationException("Unable to attend call for given room: " + req.getRoomNumber());
        }
        return rooms.get(req.getRoomNumber()).getManager();
    }

    public synchronized MMindStatusResponse getStatus(long id, String token, long roomNumber) {
        MMindTokenInfo tokenInfo = auth.get(token);
        if(tokenInfo == null || tokenInfo.getRoomNumber() != roomNumber) {
            throw new UnsupportedOperationException("Unable to attend call for given room: " + roomNumber);
        }
        MMindGameManager manager = rooms.get(roomNumber).getManager();
        MMindHistoryItem lastHistoryItem = manager.getLastHistoryItem();
        MMindStatusEventType eventType = manager.calculateEventType(token);
        MMindMoveResultDTO lastMove = lastHistoryItem != null ?
                MMindResultMapper.toResultDTO(lastHistoryItem.getMoveResult()) : null;
        MMindStatusResponse.MMindStatusResponseBuilder builder = MMindStatusResponse.builder()
                .withId(id)
                .withFailure(false)
                .withEventType(eventType.toString())
                .withLastMove(lastMove)
                .withOpponentName(manager.getOpponentsName(token))
                .withIsOwnMove(lastHistoryItem != null ? token.equals(lastHistoryItem.getPlayerToken()) : null);
        if(eventType == MMindStatusEventType.GAME_OVER_EVT) {
            builder.withResult(manager.provideEndResult())
                .withOpponentSecret(manager.provideOpponentsSecret(token));
        }
        return builder.build();
    }

    public synchronized void clean() {
        long now = System.currentTimeMillis();
        List<String> expiredTokens = auth.values().stream()
                .filter(tokenInfo -> now > tokenInfo.getExpiresAfter())
                .map(MMindTokenInfo::getToken)
                .collect(Collectors.toList());
        expiredTokens.forEach(auth::remove);
        List<Long> expiredRooms = rooms.values().stream()
                .filter(room -> now > room.getExpiresAfter())
                .map(MMindRoom::getRoomNumber)
                .collect(Collectors.toList());
        expiredRooms.forEach(rooms::remove);
        logger.info("Cleanup Report Rooms:{} Tokens:{}", expiredRooms.size(), expiredTokens.size());
    }

    public synchronized MMindSystemStatusResponse reviewSystemStatus(long id, String token) {
        if(isAdminToken(token)) {
            return MMindResultMapper.toSystemStatusResponse(id, new ArrayList<>(rooms.values()), auth);
        }
        throw new UnsupportedOperationException("Unauthorized: Unable to perform request");
    }

    public synchronized MMindJoinRoomResponse attemptAuthenticate(long id, MMindJoinRoomRequestDTO request) {
        final MMindAuthResponse authResponse = authenticate(request);
        return MMindResultMapper.toJoinResponse(id, authResponse, request.getPlayerName());
    }

    public synchronized MMindRestartResponse processRestartRequest(long id, MMindRestartRequest req) {
        final MMindTokenInfo tokenInfo = auth.get(req.getToken());
        if(tokenInfo != null) {
            final MMindRoom room = rooms.get(tokenInfo.getRoomNumber());
            if(room == null) {
                return MMindRestartResponse.builder()
                        .withId(id)
                        .withFailure(true)
                        .withAction(MMindRestartAction.CHANGE_ROOM.toString())
                        .build();
            }
            final MMindGameManager manager = room.getManager();
            if(manager.isGameOver()) {
                auth.put(req.getToken(), tokenInfo.renew());
                final MMindGameManager newManager = manager.newManager(req.getToken(), req.getSecret());
                rooms.put(room.getRoomNumber(), room.restartGame(newManager));
                return MMindRestartResponse.builder()
                        .withId(id)
                        .withFailure(false)
                        .withAction(MMindRestartAction.AWAIT_GUEST.toString())
                        .withSecret(req.getSecret())
                        .build();
            }
            if (manager.awaitingGuest()) {
                auth.put(req.getToken(), tokenInfo.renew());
                manager.addGuestWithNewSecret(req.getToken(), req.getSecret());
                return MMindRestartResponse.builder()
                        .withId(id)
                        .withFailure(false)
                        .withSecret(req.getSecret())
                        .withAction(MMindRestartAction.AWAIT_MOVE.toString())
                        .build();
            }
            //clean token
            auth.remove(req.getToken());
        }
        return MMindRestartResponse.builder()
                .withId(id)
                .withFailure(true)
                .withAction(MMindRestartAction.CHANGE_ROOM.toString())
                .build();
    }

    /**
     * Attempts to remove a token does not belong to any room
     */
    public synchronized SimpleResponse processDeleteTokenRequest(long id, MMindDeleteTokenRequest req) {
        final String tokenToDelete = req.getTokenToDelete();
        if(!req.getUserToken().equals(tokenToDelete)) {
            if(isAdminToken(req.getUserToken())) {
                final boolean unusedToken = rooms.values().stream()
                        .noneMatch(r -> r.hasPlayerWithToken(req.getTokenToDelete()));
                if(unusedToken) {
                    auth.remove(req.getTokenToDelete());
                    return MMindDeleteTokenResponse.builder()
                            .withId(id)
                            .withFailure(false)
                            .build();
                }
            }
        }
        return MMindDeleteTokenResponse.builder()
                .withId(id)
                .withFailure(true)
                .withMessage("unable to delete token as it might be in use")
                .build();
    }

    public synchronized MMindDeleteRoomResponse processDeleteRoomRequest(long id, MMindDeleteRoomRequest req) {
        MMindRoom room = rooms.get(req.getRoomNumber());
        if(null != room && room.getRoomNumber() != auth.get(req.getToken()).getRoomNumber()) {
            String guestToken = room.getManager().retrieveGuestToken();
            if(auth.get(guestToken) != null) {
                auth.remove(guestToken);
            }
            String hostToken = room.getManager().retrieveHostToken();
            if (auth.get(hostToken) != null) {
                auth.remove(hostToken);
            }
            rooms.remove(room.getRoomNumber());
            return MMindDeleteRoomResponse.builder()
                    .withMessage("room deleted :" + room.getRoomNumber())
                    .withId(id)
                    .withFailure(false)
                    .build();
        }
        String msg = ": " + ((room == null) ? "not found" : "can't delete current room");
        return MMindDeleteRoomResponse.builder()
                .withId(id)
                .withFailure(true)
                .withMessage("unable to delete room: " + req.getRoomNumber() + msg)
                .build();
    }
}
