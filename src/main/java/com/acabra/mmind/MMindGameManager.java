package com.acabra.mmind;

import com.acabra.mmind.core.*;
import com.acabra.mmind.request.MMindJoinRoomRequestDTO;
import com.acabra.mmind.request.MMindRequestDTO;
import com.acabra.mmind.response.MMindMoveResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MMindGameManager {

    private final Map<String, MMindPlayer> players;
    private final Map<String, MMindPlayer> secretHolders;
    private final MMindPlayer host;
    private volatile int currentMove = Moves.NONE;
    private final ArrayList<MMindHistoryItem> moves;
    private final AtomicInteger movesRegistry;

    private MMindGameManager(MMindPlayer host) {
        this.host = host;
        this.players = new HashMap<>();
        this.secretHolders = new HashMap<>();
        this.players.put(host.getToken(), host);
        this.moves = new ArrayList<>();
        this.movesRegistry = new AtomicInteger(0);
    }

    public static MMindGameManager newGame(MMindPlayer host) {
        return new MMindGameManager(host);
    }

    public MMindMoveResponse attemptMove(long responseId, MMindRequestDTO requestDTO) {
        char[] guess = requestDTO.getGuess().toCharArray();
        MMindPlayer secretHolder = secretHolders.get(requestDTO.getToken());
        MMindPlayer guesser = players.get(requestDTO.getToken());
        MMmindMoveResult moveResult = secretHolder.respond(movesRegistry.getAndIncrement(), guesser.move(), guess);
        moveResult.setPlayerName(guesser.getName());
        if(host.getToken().equals(requestDTO.getToken())) {
            currentMove = Moves.GUEST;
        } else {
            currentMove = Moves.HOST;
        }
        moves.add(MMindHistoryItem.builder()
                .withMoveResult(moveResult)
                .withPlayerToken(requestDTO.getToken())
                .build());
        return MMindMoveResponse.ok(responseId, isGameOver(), MMindResultMapper.toResultDTO(moveResult));
    }

    private boolean isGameOver(MMmindMoveResult p1MoveResult, MMmindMoveResult p2MoveResult) {
        return (p2MoveResult.getIndex() == p1MoveResult.getIndex()
                && (p1MoveResult.getFixes() == 4 || p2MoveResult.getFixes() == 4));
    }

    boolean isGameOver() {
        if(moves.size() < 2) return false;
        MMmindMoveResult p1MoveResult = moves.get(moves.size()-1).getMoveResult();
        MMmindMoveResult p2MoveResult = moves.get(moves.size()-2).getMoveResult();
        return isGameOver(p1MoveResult, p2MoveResult);
    }

    public boolean awaitingGuest() {
        return secretHolders.isEmpty();
    }

    public void addGuest(MMindPlayer guest) {
        players.put(guest.getToken(), guest);
        secretHolders.put(guest.getToken(), host);
        secretHolders.put(host.getToken(), guest);
        currentMove = Moves.HOST;
    }

    public String retrieveHostToken() {
        return host.getToken();
    }

    public boolean hasMove(String token) {
        if(currentMove == 0) {
            return false;
        }
        if(currentMove == 1 && host.getToken().equals(token)) {
            return true;
        }
        return currentMove == 2 && token.equals(getGuest().getToken());
    }

    public MMindHistoryItem getLastHistoryItem() {
        return moves.isEmpty() ? null : moves.get(moves.size() - 1);
    }

    public boolean isCurrentGuest(MMindJoinRoomRequestDTO requestDTO) {
        final MMindPlayer guest = getGuest();
        return guest != null
                && guest.getToken().equals(requestDTO.getToken())
                && Arrays.equals(guest.getSecret(), requestDTO.getSecret().toCharArray())
                && guest.getName().equals(requestDTO.getPlayerName());
    }

    private MMindPlayer getGuest() {
        return secretHolders.get(host.getToken());
    }

    public String provideEndResult() {
        if(isGameOver()) {
            MMmindMoveResult p1MoveResult = moves.get(moves.size()-1).getMoveResult();
            MMmindMoveResult p2MoveResult = moves.get(moves.size()-2).getMoveResult();
            if(isGameOver(p1MoveResult, p2MoveResult)) {
                int res = Integer.compare(p1MoveResult.getFixes(), p2MoveResult.getFixes());
                if(res == 0) {
                    return "Tie";
                }
                return res < 0 ? p2MoveResult.getPlayerName() : p1MoveResult.getPlayerName();
            }
        }
        return null;
    }

    public String retrieveGuestToken() {
        if(players.size() < 2) return null;
        final MMindPlayer guest = getGuest();
        return guest == null ? null : guest.getToken();
    }

    public String getOpponentsName(String token) {
        MMindPlayer player = secretHolders.get(token);
        return player != null ? player.getName() : null;
    }

    public MMindStatusEventType calculateEventType(String token) {
        if(isGameOver()) return MMindStatusEventType.GAME_OVER_EVT;
        if(hasMove(token)) return MMindStatusEventType.MAKE_MOVE_EVT;
        return MMindStatusEventType.AWAIT_RESTART_EVT;
    }

    public MMindGameManager newManager(String hostSecret) {
        final MMindGameManager newManager = MMindGameManager.newGame(host.newSecret(hostSecret));
        MMindPlayer oldGuest = getGuest();
        newManager.players.put(oldGuest.getToken(), oldGuest);
        return newManager;
    }

    public void addGuestWithNewSecret(String token, String secret) {
        final MMindPlayer oldPlayer = players.get(token);
        addGuest(oldPlayer.newSecret(secret));
    }
}
