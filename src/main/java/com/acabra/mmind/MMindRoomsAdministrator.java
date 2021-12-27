package com.acabra.mmind;

import com.acabra.mmind.auth.MMindTokenInfo;
import com.acabra.mmind.core.AuthAction;
import com.acabra.mmind.core.MMindPlayer;
import com.acabra.mmind.core.MMindRoom;
import com.acabra.mmind.request.MMindJoinRoomRequestDTO;
import com.acabra.mmind.request.MMindRequestDTO;
import com.acabra.mmind.response.MMindAuthResponse;
import com.acabra.mmind.response.MMindMoveResultDTO;
import com.acabra.mmind.response.MMindStatusResponse;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MMindRoomsAdministrator {
    private static final String ADM_PWD = System.getenv("admpwd");
    public static final Base64.Decoder B64_DEC = Base64.getDecoder();
    private final Map<Long, MMindRoom> rooms;
    private final Map<String, MMindTokenInfo> auth;


    private MMindRoomsAdministrator() {
        this.rooms = new HashMap<>();
        this.auth = new HashMap<>();
    }

    public static MMindRoomsAdministrator of() {
        return new MMindRoomsAdministrator();
    }

    public synchronized MMindAuthResponse authenticate(MMindJoinRoomRequestDTO request) {
        long roomNumber = request.getRoomNumber();
        MMindRoom room = rooms.get(roomNumber);
        boolean admin = isAdmin(request);
        if(null == room) {
            if(admin) {
                return createRoomAsAdmin(request, roomNumber);
            }
            throw new NoSuchElementException("Unable to locate given room: " + roomNumber);
        }
        String password = decode(request.getPassword());
        if(admin) {
            return MMindAuthResponse.builder()
                    .withToken(room.getManager().retrieveHostToken())
                    .withRoomPassword(password)
                    .withRoomNumber(room.getRoomNumber())
                    .withAction(AuthAction.NONE)
                    .build();
        }
        if(room.getManager().awaitingGuest() && password.equals(room.getPassword())) {
            return joinRoomAsGuest(request, room, password);
        }
        throw new UnsupportedOperationException("Room is full");
    }

    private MMindAuthResponse joinRoomAsGuest(MMindJoinRoomRequestDTO request, MMindRoom room, String password) {
        String token = UUID.randomUUID().toString();
        MMindPlayer player = new MMindPlayer(request.getPlayerName(), request.getSecret(), token);
        auth.put(token, MMindTokenInfo.builder()
                        .withRoomNumber(room.getRoomNumber())
                        .withToken(token)
                        .withExpiresAfter(newTokenExpiration())
                .build());
        room.getManager().addGuest(player);
        return MMindAuthResponse.builder()
                .withToken(token)
                .withRoomPassword(password)
                .withRoomNumber(room.getRoomNumber())
                .withAction(AuthAction.JOIN_GUEST)
                .build();
    }

    private long newTokenExpiration() {
        return System.currentTimeMillis() + TimeUnit.MINUTES.convert(30, TimeUnit.MILLISECONDS);
    }

    private MMindAuthResponse createRoomAsAdmin(MMindJoinRoomRequestDTO joinRoomRequest, long roomNumber) {
        String token = UUID.randomUUID().toString();
        long expiresAfter = newTokenExpiration();
        auth.put(token, MMindTokenInfo.builder()
                .withRoomNumber(roomNumber)
                .withToken(token)
                .withExpiresAfter(expiresAfter)
                .build());
        MMindPlayer player = new MMindPlayer(joinRoomRequest.getPlayerName(), joinRoomRequest.getSecret(), token);
        MMindRoom room = MMindRoom.builder()
                .withRoomNumber(roomNumber)
                .withManager(MMindGameManager.newGame(player))
                .withExpiresAfter(expiresAfter)
                .build();
        rooms.put(room.getRoomNumber(), room);
        return MMindAuthResponse.builder()
                .withToken(token)
                .withRoomPassword(room.getPassword())
                .withRoomNumber(room.getRoomNumber())
                .withAction(AuthAction.JOIN_ADMIN)
                .build();
    }

    private String decode(String encodedString) {
        return new String(B64_DEC.decode(encodedString), StandardCharsets.UTF_8);
    }

    private boolean isAdmin(MMindJoinRoomRequestDTO joinRoomRequest) {
        return ADM_PWD.equals(decode(joinRoomRequest.getPassword()));
    }

    public MMindGameManager findRoomManager(MMindRequestDTO req) {
        MMindTokenInfo tokenInfo = auth.get(req.getToken());
        if(tokenInfo == null || tokenInfo.getRoomNumber() != req.getRoomNumber()) {
            throw new UnsupportedOperationException("Unable to attend call for given room: " + req.getRoomNumber());
        }
        return rooms.get(req.getRoomNumber()).getManager();
    }

    public MMindStatusResponse getStatus(long id, String token, long roomNumber) {
        MMindTokenInfo tokenInfo = auth.get(token);
        if(tokenInfo == null || tokenInfo.getRoomNumber() != roomNumber) {
            throw new UnsupportedOperationException("Unable to attend call for given room: " + roomNumber);
        }
        MMindGameManager manager = rooms.get(roomNumber).getManager();
        boolean makeMove = manager.hasMove(token);
        MMindMoveResultDTO lastMove = makeMove ? MMindResultMapper.toResultDTO(manager.getLastMove()) : null;
        return MMindStatusResponse.builder()
                .withFailure(false)
                .withId(id)
                .withMakeMove(makeMove)
                .withLastMove(lastMove)
                .build();
    }

    public void clean() {
        long now = System.currentTimeMillis();
        long expiredEntries = now - TimeUnit.MINUTES.convert(30, TimeUnit.MILLISECONDS);
    }
}
