package com.acabra.mmind;

import com.acabra.mmind.core.*;
import com.acabra.mmind.request.MMindRequestDTO;
import com.acabra.mmind.response.MMindMoveResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MMindGameManager {

    public static final int HOST_IDX = 0;
    private static final int GUEST_IDX = 1;
    private final Map<String, MMindPlayer> secretHolders;
    private final MMindPlayer[] players;
    private volatile int currentMove = Moves.NONE;
    private final ArrayList<MMindHistoryItem> moves;
    private final AtomicInteger movesRegistry;

    private MMindGameManager(MMindPlayer host) {
        this.players = new MMindPlayer[2];
        this.secretHolders = new HashMap<>();
        this.players[HOST_IDX] = host;
        this.moves = new ArrayList<>();
        this.movesRegistry = new AtomicInteger(0);
    }

    public static MMindGameManager newGame(MMindPlayer host) {
        return new MMindGameManager(host);
    }

    private MMindPlayer getGuest() {
        return players[GUEST_IDX];
    }

    private MMindPlayer getHost() {
        return players[HOST_IDX];
    }

    private int nextTurn(String guesserToken) {
        if (guesserToken.equals(getHost().getToken())) {
            return Moves.GUEST;
        }
        return Moves.HOST;
    }

    private MMindPlayer getPlayerByToken(String token) {
        MMindPlayer host = getHost();
        if (host.getToken().equals(token)) {
            return host;
        }
        return getGuest();
    }

    private boolean isGameOver(MMindMoveResult p1MoveResult, MMindMoveResult p2MoveResult) {
        return (p2MoveResult.getIndex() == p1MoveResult.getIndex()
                && (p1MoveResult.getFixes() == 4 || p2MoveResult.getFixes() == 4));
    }

    public MMindMoveResponse attemptMove(long responseId, MMindRequestDTO requestDTO) {
        char[] guess = requestDTO.getGuess().toCharArray();
        MMindPlayer secretHolder = secretHolders.get(requestDTO.getToken());
        MMindPlayer guesser = getPlayerByToken(requestDTO.getToken());
        MMindMoveResult moveResult = secretHolder.respond(movesRegistry.getAndIncrement(), guesser.move(), guess);
        moveResult.setPlayerName(guesser.getName());
        currentMove = nextTurn(requestDTO.getToken());
        moves.add(MMindHistoryItem.builder()
                .withMoveResult(moveResult)
                .withPlayerToken(requestDTO.getToken())
                .withPlayerId(guesser.getId())
                .build());
        return MMindMoveResponse.ok(responseId, isGameOver(), MMindResultMapper.toResultDTO(moveResult));
    }

    public boolean isGameOver() {
        if (moves.size() < 2) return false;
        MMindMoveResult p1MoveResult = moves.get(moves.size() - 1).getMoveResult();
        MMindMoveResult p2MoveResult = moves.get(moves.size() - 2).getMoveResult();
        return isGameOver(p1MoveResult, p2MoveResult);
    }

    public boolean awaitingGuest() {
        return secretHolders.isEmpty();
    }

    public void addGuest(MMindPlayer guest) {
        players[GUEST_IDX] = guest;
        MMindPlayer host = getHost();
        secretHolders.put(guest.getToken(), host);
        secretHolders.put(host.getToken(), guest);
        currentMove = Moves.HOST;
    }

    public String retrieveHostToken() {
        return getHost().getToken();
    }

    public boolean hasMove(String token) {
        if (currentMove == 0) {
            return false;
        }
        if (currentMove == 1 && getHost().getToken().equals(token)) {
            return true;
        }
        return currentMove == 2 && token.equals(getGuest().getToken());
    }

    public MMindHistoryItem getLastHistoryItem() {
        return moves.isEmpty() ? null : moves.get(moves.size() - 1);
    }

    public Long provideEndResult() {
        if (isGameOver()) {
            final MMindHistoryItem p1MoveHistoryItem = moves.get(moves.size() - 1);
            final MMindHistoryItem p2MoveHistoryItem = moves.get(moves.size() - 2);
            MMindMoveResult p1MoveResult = p1MoveHistoryItem.getMoveResult();
            MMindMoveResult p2MoveResult = p2MoveHistoryItem.getMoveResult();
            if (isGameOver(p1MoveResult, p2MoveResult)) {
                int res = Integer.compare(p1MoveResult.getFixes(), p2MoveResult.getFixes());
                if (res == 0) {
                    return -1L;
                }
                return res < 0 ? p2MoveHistoryItem.getPlayerId() : p1MoveHistoryItem.getPlayerId();
            }
        }
        return null;
    }

    public String retrieveGuestToken() {
        final MMindPlayer guest = getGuest();
        return guest == null ? null : guest.getToken();
    }

    public String getOpponentsName(String token) {
        MMindPlayer player = secretHolders.get(token);
        return player != null ? player.getName() : null;
    }

    public MMindStatusEventType calculateEventType(String token) {
        if (isGameOver()) return MMindStatusEventType.GAME_OVER_EVT;
        if (hasMove(token)) return MMindStatusEventType.MAKE_MOVE_EVT;
        return MMindStatusEventType.AWAIT_RESTART_EVT;
    }

    public MMindGameManager newManager(String token, String playerSecret) {
        final MMindPlayer host = getHost();
        final boolean currentHostRequestsRestart = token.equals(host.getToken());
        MMindPlayer newHost = currentHostRequestsRestart ? host.newSecret(playerSecret) : getGuest().newSecret(playerSecret);
        MMindPlayer newGuest = currentHostRequestsRestart? getGuest(): host;
        final MMindGameManager newManager = MMindGameManager.newGame(newHost);
        newManager.players[GUEST_IDX] = newGuest;
        return newManager;
    }

    public void addGuestWithNewSecret(String token, String secret) {
        final MMindPlayer playerByToken = getPlayerByToken(token);
        if(!playerByToken.getToken().equals(getGuest().getToken())) {
            throw new IllegalArgumentException("Can't add as guest the player !!!");
        }
        addGuest(playerByToken.newSecret(secret));
    }

    public String getHostName() {
        return getHost().getName();
    }

    public String getGuestName() {
        return getGuest().getName();
    }
}
