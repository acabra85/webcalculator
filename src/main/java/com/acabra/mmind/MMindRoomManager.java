package com.acabra.mmind;

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

public class MMindRoomManager {
    private static final String ADM_PWD = System.getenv("admpwd");
    public static final Base64.Decoder B64_DEC = Base64.getDecoder();
    private final Map<Long, MMindRoom> rooms;
    private final Map<String, Long> auth;


    private MMindRoomManager() {
        this.rooms = new HashMap<>();
        this.auth = new HashMap<>();
    }

    public static MMindRoomManager of() {
        return new MMindRoomManager();
    }

    public synchronized MMindAuthResponse authenticate(MMindJoinRoomRequestDTO joinRoomRequest) {
        Long roomNumber = joinRoomRequest.getRoomNumber();
        MMindRoom mMindRoom = rooms.get(roomNumber);
        boolean admin = isAdmin(joinRoomRequest);
        if(null == mMindRoom) {
            if(admin) {
                String token = UUID.randomUUID().toString();
                auth.put(token, roomNumber);
                MMindPlayer player = new MMindPlayer(joinRoomRequest.getPlayerName(), joinRoomRequest.getSecret(), token);
                MMindRoom room = MMindRoom.builder()
                        .withRoomNumber(roomNumber)
                        .withManager(MMindManager.newGame(player))
                        .build();
                rooms.put(room.getRoomNumber(), room);
                return MMindAuthResponse.builder()
                        .withToken(token)
                        .withRoomPassword(room.getPassword())
                        .withRoomNumber(room.getRoomNumber())
                        .withAction(AuthAction.JOIN_ADMIN)
                        .build();
            }
            throw new NoSuchElementException("Unable to locate given room: " + roomNumber);
        }
        MMindManager manager = mMindRoom.getManager();
        String password = decode(joinRoomRequest.getPassword());
        if(admin) {
            return MMindAuthResponse.builder()
                    .withToken(manager.retrieveHostToken())
                    .withRoomPassword(password)
                    .withRoomNumber(mMindRoom.getRoomNumber())
                    .withAction(AuthAction.NONE)
                    .build();
        }
        if(manager.awaitingPlayer() && password.equals(mMindRoom.getPassword())) {
            String token = UUID.randomUUID().toString();
            MMindPlayer player = new MMindPlayer(joinRoomRequest.getPlayerName(), joinRoomRequest.getSecret(), token);
            auth.put(token, roomNumber);
            manager.addGuest(player);
            return MMindAuthResponse.builder()
                    .withToken(token)
                    .withRoomPassword(password)
                    .withRoomNumber(mMindRoom.getRoomNumber())
                    .withAction(AuthAction.JOIN_GUEST)
                    .build();
        }
        throw new UnsupportedOperationException("Room is full");
    }

    private String decode(String encodedString) {
        return new String(B64_DEC.decode(encodedString), StandardCharsets.UTF_8);
    }

    private boolean isAdmin(MMindJoinRoomRequestDTO joinRoomRequest) {
        return ADM_PWD.equals(decode(joinRoomRequest.getPassword()));
    }

    public MMindRoom findRoom(MMindRequestDTO req) {
        Long rNumber = auth.get(req.getToken());
        if(rNumber != req.getRoomNumber()) {
            throw new UnsupportedOperationException("Unable to attend call for given room: " + req.getRoomNumber());
        }
        return rooms.get(rNumber);
    }

    public MMindStatusResponse getStatus(long id, String token, long roomNumber) {
        Long rNumber = auth.get(token);
        if(rNumber == null || rNumber != roomNumber) {
            throw new UnsupportedOperationException("Unable to attend call for given room: " + roomNumber);
        }
        MMindManager manager = rooms.get(roomNumber).getManager();
        boolean makeMove = manager.hasMove(token);
        MMindMoveResultDTO lastMove = makeMove ? MMindResultMapper.toResultDTO(manager.getLastMove()) : null;
        return MMindStatusResponse.builder()
                .withFailure(false)
                .withId(id)
                .withMakeMove(makeMove)
                .withLastMove(lastMove)
                .build();
    }
}
