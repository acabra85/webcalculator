package com.acabra.fsands;

import com.acabra.calculator.response.SimpleResponse;
import com.acabra.fsands.auth.ConstantTimePasswordChecker;
import com.acabra.fsands.auth.FixSpikeTokenInfo;
import com.acabra.fsands.core.*;
import com.acabra.fsands.request.*;
import com.acabra.fsands.response.*;
import com.acabra.fsands.utils.B64Helper;
import com.acabra.fsands.utils.TimeDateHelper;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
public class FixSpikeRoomsAdministrator {
    private static final String ADM_PWD = System.getenv("admpwd");
    private final Map<Long, FixSpikeRoom> rooms;
    private final Map<String, FixSpikeTokenInfo> auth;
    private final AtomicLong playerIdGen;

    private FixSpikeRoomsAdministrator() {
        this.rooms = new HashMap<>();
        this.auth = new HashMap<>();
        this.playerIdGen = new AtomicLong();
    }

    private FixSpikeAuthResponse joinRoomAsGuest(FixSpikeJoinRoomRequestDTO request, FixSpikeRoom room, boolean isAdmin) {
        String token = UUID.randomUUID().toString();
        FixSpikePlayer player = new FixSpikePlayer(playerIdGen.getAndIncrement(), request.getPlayerName(), request.getSecret(), token);
        auth.put(token, FixSpikeTokenInfo.builder()
                .withAdminToken(isAdmin)
                .withRoomNumber(room.getRoomNumber())
                .withToken(token)
                .withExpiresAfter(newTokenExpiration())
                .build());
        room.getManager().addGuest(player);
        return FixSpikeAuthResponse.builder()
                .withToken(token)
                .withPlayerId(player.getId())
                .withHostName(room.getHostName())
                .withRoomPassword(room.getPassword())
                .withRoomNumber(room.getRoomNumber())
                .withAction(isAdmin ? FixSpikeAuthAction.JOIN_ADMIN : FixSpikeAuthAction.JOIN_GUEST)
                .build();
    }

    private long newTokenExpiration() {
        return System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30);
    }

    private FixSpikeAuthResponse hostARoom(FixSpikeJoinRoomRequestDTO joinRoomRequest, long roomNumber, boolean isAdmin) {
        String token = UUID.randomUUID().toString();
        long expiresAfter = newTokenExpiration();
        logger.info("--------------->>> Created room {} expires after:{}" , roomNumber, TimeDateHelper.fromEpoch(expiresAfter));
        auth.put(token, FixSpikeTokenInfo.builder()
                .withAdminToken(isAdmin)
                .withRoomNumber(roomNumber)
                .withToken(token)
                .withExpiresAfter(expiresAfter)
                .build());
        FixSpikePlayer player = new FixSpikePlayer(playerIdGen.getAndIncrement(),
                joinRoomRequest.getPlayerName(), joinRoomRequest.getSecret(), token);
        FixSpikeRoom room = FixSpikeRoom.builder()
                .withRoomNumber(roomNumber)
                .withManager(FixSpikeGameManager.newGame(player))
                .withExpiresAfter(expiresAfter)
                .build();
        rooms.put(room.getRoomNumber(), room);
        return FixSpikeAuthResponse.builder()
                .withToken(token)
                .withPlayerId(player.getId())
                .withRoomPassword(room.getPassword())
                .withRoomNumber(room.getRoomNumber())
                .withAction(isAdmin ? FixSpikeAuthAction.JOIN_ADMIN : FixSpikeAuthAction.JOIN_HOST)
                .build();
    }

    private boolean isAdminPassword(String password) {
        return ConstantTimePasswordChecker.check(ADM_PWD, password);
    }

    private boolean isAdminToken(String token) {
        FixSpikeTokenInfo tokenInfo = auth.get(token);
        return tokenInfo != null && tokenInfo.isAdminToken();
    }

    private synchronized FixSpikeAuthResponse authenticate(FixSpikeJoinRoomRequestDTO request) {
        FixSpikeRoom room = rooms.get(request.getRoomNumber());
        String password = B64Helper.decode(request.getPassword());
        boolean isAdmin = isAdminPassword(password);
        if(null == room) {
            return hostARoom(request, request.getRoomNumber(), isAdmin);
        }
        if(room.getManager().hostWaitingForGuest() || room.getManager().gameHasNotStarted()) {
            if((isAdmin || ConstantTimePasswordChecker.check(room.getPassword(), password))) {
                if(!room.hasPlayerWithToken(request.getToken())) {
                    return joinRoomAsGuest(request, room, isAdmin);
                }
            }
            throw new UnsupportedOperationException("Unable to join, wrong password for room: " + request.getRoomNumber());
        }
        throw new UnsupportedOperationException("Unable to join, room is full: " + request.getRoomNumber());
    }

    public static FixSpikeRoomsAdministrator of() {
        return new FixSpikeRoomsAdministrator();
    }

    public synchronized FixSpikeGameManager findRoomManager(FixSpikeRequestDTO req) {
        FixSpikeTokenInfo tokenInfo = auth.get(req.getToken());
        if(tokenInfo == null || tokenInfo.getRoomNumber() != req.getRoomNumber()) {
            throw new UnsupportedOperationException("Unable to attend call for given room: " + req.getRoomNumber());
        }
        return rooms.get(req.getRoomNumber()).getManager();
    }

    public synchronized FixSpikeStatusResponseDTO getStatus(long id, Long seq, String token, long roomNumber) {
        FixSpikeTokenInfo tokenInfo = auth.get(token);
        if(tokenInfo == null || tokenInfo.getRoomNumber() != roomNumber) {
            throw new UnsupportedOperationException("Unable to attend call for given room: " + roomNumber);
        }
        FixSpikeGameManager manager = rooms.get(roomNumber).getManager();
        FixSpikeGameState gameState = manager.provideGameState(seq, token);
        return FixSpikeStatusResponseDTO.builder()
                .withId(id)
                .withFailure(false)
                .withGameStatus(gameState.getGameStatus())
                .withIsOwnMove(gameState.getIsOwnMove())
                .withLastMove(FixSpikeResultMapper.toMoveResultDTO(gameState.getLastMove()))
                .withOpponentName(gameState.getOpponentName())
                .withSequenceId(gameState.getSequenceId())
                .build();
    }

    public synchronized void clean() {
        long now = System.currentTimeMillis();
        List<String> expiredTokens = auth.values().stream()
                .filter(tokenInfo -> now > tokenInfo.getExpiresAfter())
                .map(FixSpikeTokenInfo::getToken)
                .collect(Collectors.toList());
        expiredTokens.forEach(auth::remove);
        List<Long> expiredRooms = rooms.values().stream()
                .filter(room -> now > room.getExpiresAfter())
                .map(FixSpikeRoom::getRoomNumber)
                .collect(Collectors.toList());
        expiredRooms.forEach(rooms::remove);
        logger.info("Cleanup Report Rooms:{} Tokens:{}", expiredRooms.size(), expiredTokens.size());
    }

    public synchronized FixSpikeSystemStatusResponse reviewSystemStatus(long id, String token) {
        if(isAdminToken(token)) {
            return FixSpikeResultMapper.toSystemStatusResponse(id, new ArrayList<>(rooms.values()), auth);
        }
        throw new UnsupportedOperationException("Unauthorized: Unable to perform request");
    }

    public synchronized FixSpikeJoinRoomResponse attemptAuthenticate(long id, FixSpikeJoinRoomRequestDTO request) {
        final FixSpikeAuthResponse authResponse = authenticate(request);
        return FixSpikeResultMapper.toJoinResponse(id, authResponse, request.getPlayerName());
    }

    public synchronized FixSpikeRestartResponse processRestartRequest(long id, FixSpikeRestartRequest req) {
        final FixSpikeTokenInfo tokenInfo = auth.get(req.getToken());
        if(tokenInfo != null) {
            final FixSpikeRoom room = rooms.get(tokenInfo.getRoomNumber());
            if(room == null) {
                return FixSpikeRestartResponse.builder()
                        .withId(id)
                        .withFailure(true)
                        .withAction(FixSpikeRestartAction.CHANGE_ROOM.toString())
                        .build();
            }
            final FixSpikeGameManager manager = room.getManager();
            if(manager.isGameOver() || manager.lastMoveExit()) {
                auth.put(req.getToken(), tokenInfo.renew());
                final FixSpikeGameManager newManager = manager.newManager(req.getToken(), req.getSecret());
                rooms.put(room.getRoomNumber(), room.restartGame(newManager));
                return FixSpikeRestartResponse.builder()
                        .withId(id)
                        .withFailure(false)
                        .withSecret(req.getSecret())
                        .withAction(FixSpikeRestartAction.AWAIT_GUEST.toString())
                        .build();
            }
            if (manager.hostWaitingForGuest()) {
                auth.put(req.getToken(), tokenInfo.renew());
                manager.addGuestWithNewSecret(req.getToken(), req.getSecret());
                return FixSpikeRestartResponse.builder()
                        .withId(id)
                        .withFailure(false)
                        .withSecret(req.getSecret())
                        .withAction(FixSpikeRestartAction.AWAIT_MOVE.toString())
                        .build();
            }
            //clean token
            auth.remove(req.getToken());
        }
        return FixSpikeRestartResponse.builder()
                .withId(id)
                .withFailure(true)
                .withAction(FixSpikeRestartAction.CHANGE_ROOM.toString())
                .build();
    }

    /**
     * Attempts to remove a token does not belong to any room
     */
    public synchronized SimpleResponse processDeleteTokenRequest(long id, FixSpikeDeleteTokenRequest req) {
        final String tokenToDelete = req.getTokenToDelete();
        if(!req.getUserToken().equals(tokenToDelete)) {
            if(isAdminToken(req.getUserToken())) {
                final boolean unusedToken = rooms.values().stream()
                        .noneMatch(r -> r.hasPlayerWithToken(req.getTokenToDelete()));
                if(unusedToken) {
                    auth.remove(req.getTokenToDelete());
                    return FixSpikeDeleteTokenResponse.builder()
                            .withId(id)
                            .withFailure(false)
                            .build();
                }
            }
        }
        return FixSpikeDeleteTokenResponse.builder()
                .withId(id)
                .withFailure(true)
                .withMessage("unable to delete token as it might be in use")
                .build();
    }

    public synchronized FixSpikeDeleteRoomResponse processDeleteRoomRequest(long id, FixSpikeDeleteRoomRequest req) {
        FixSpikeRoom room = rooms.get(req.getRoomNumber());
        if(null != room) {
            String guestToken = room.getManager().retrieveGuestToken();
            if(auth.get(guestToken) != null) {
                auth.remove(guestToken);
            }
            String hostToken = room.getManager().retrieveHostToken();
            if (auth.get(hostToken) != null) {
                auth.remove(hostToken);
            }
            rooms.remove(room.getRoomNumber());
            return FixSpikeDeleteRoomResponse.builder()
                    .withMessage("room deleted :" + room.getRoomNumber())
                    .withId(id)
                    .withFailure(false)
                    .build();
        }
        String msg = ": not found";
        return FixSpikeDeleteRoomResponse.builder()
                .withId(id)
                .withFailure(true)
                .withMessage("unable to delete room: " + req.getRoomNumber() + msg)
                .build();
    }

    public synchronized SimpleResponse exitRoom(long id, String token, long roomNumber) {
        FixSpikeTokenInfo tokenInfo = auth.get(token);
        if(tokenInfo != null && tokenInfo.getRoomNumber() == roomNumber) {
            FixSpikeRoom room = rooms.get(tokenInfo.getRoomNumber());
            FixSpikeGameManager manager = room.getManager();
            if(manager.hostWaitingForGuest()|| manager.lastMoveExit()) {
                rooms.remove(room.getRoomNumber());
            } else {
                manager.playerExit(token);
            }
            auth.remove(token);
            return okResponse(id);
        }
        return failResponse(id);
    }

    public SimpleResponse failResponse(long id) {
        return simpleResponse(id, true);
    }

    public SimpleResponse okResponse(long id) {
        return simpleResponse(id, false);
    }

    private SimpleResponse simpleResponse(long id, boolean isFailure) {
        return new SimpleResponse(id, isFailure) {
            private static final long serialVersionUID = -2324953824486824277L;
            @Override
            public long getId() {
                return id;
            }
            @Override
            public boolean isFailure() {
                return isFailure;
            }
        };
    }
}
