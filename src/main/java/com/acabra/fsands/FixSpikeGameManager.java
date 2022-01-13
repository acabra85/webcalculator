package com.acabra.fsands;

import com.acabra.fsands.core.*;
import com.acabra.fsands.request.FixSpikeRequestDTO;
import com.acabra.fsands.response.FixSpikeMoveResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class FixSpikeGameManager {

    public static final int HOST_IDX = 0;
    private static final int GUEST_IDX = 1;
    private final Map<String, FixSpikePlayer> secretHolders;
    private final FixSpikePlayer[] players;
    private volatile int currentMove = Moves.NONE;
    private final ArrayList<FixSpikeHistoryItem> moves;
    private final AtomicInteger movesRegistry;

    private FixSpikeGameManager(FixSpikePlayer host) {
        this.players = new FixSpikePlayer[2];
        this.secretHolders = new HashMap<>();
        this.players[HOST_IDX] = host;
        this.moves = new ArrayList<>();
        this.movesRegistry = new AtomicInteger(0);
    }

    public static FixSpikeGameManager newGame(FixSpikePlayer host) {
        return new FixSpikeGameManager(host);
    }

    private FixSpikePlayer getGuest() {
        return players[GUEST_IDX];
    }

    private FixSpikePlayer getHost() {
        return players[HOST_IDX];
    }

    private int nextTurn(String guesserToken) {
        if (guesserToken.equals(getHost().getToken())) {
            return Moves.GUEST;
        }
        return Moves.HOST;
    }

    private FixSpikePlayer getPlayerByToken(String token) {
        FixSpikePlayer host = getHost();
        if (host.getToken().equals(token)) {
            return host;
        }
        return getGuest();
    }

    private boolean isGameOver(FixSpikeMoveResult p1MoveResult, FixSpikeMoveResult p2MoveResult) {
        return (p2MoveResult.getIndex() == p1MoveResult.getIndex()
                && (p1MoveResult.getFixes() == 4 || p2MoveResult.getFixes() == 4));
    }

    public FixSpikeMoveResponse attemptMove(long responseId, FixSpikeRequestDTO requestDTO) {
        char[] guess = requestDTO.getGuess().toCharArray();
        FixSpikePlayer secretHolder = secretHolders.get(requestDTO.getToken());
        FixSpikePlayer guesser = getPlayerByToken(requestDTO.getToken());
        FixSpikeMoveResult moveResult = secretHolder.respond(movesRegistry.getAndIncrement(), guesser.move(), guess);
        moveResult.setPlayerName(guesser.getName());
        currentMove = nextTurn(requestDTO.getToken());
        moves.add(FixSpikeHistoryItem.builder()
                .withMoveResult(moveResult)
                .withPlayerToken(requestDTO.getToken())
                .withPlayerId(guesser.getId())
                .build());
        return FixSpikeMoveResponse.ok(responseId, isGameOver(), FixSpikeResultMapper.toResultDTO(moveResult));
    }

    public boolean isGameOver() {
        if (moves.size() < 2) return false;
        FixSpikeMoveResult p1MoveResult = moves.get(moves.size() - 1).getMoveResult();
        FixSpikeMoveResult p2MoveResult = moves.get(moves.size() - 2).getMoveResult();
        return isGameOver(p1MoveResult, p2MoveResult);
    }

    public boolean awaitingGuest() {
        return secretHolders.isEmpty();
    }

    public void addGuest(FixSpikePlayer guest) {
        players[GUEST_IDX] = guest;
        FixSpikePlayer host = getHost();
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

    public FixSpikeHistoryItem getLastHistoryItem() {
        return moves.isEmpty() ? null : moves.get(moves.size() - 1);
    }

    public Long provideEndResult() {
        if (isGameOver()) {
            final FixSpikeHistoryItem p1MoveHistoryItem = moves.get(moves.size() - 1);
            final FixSpikeHistoryItem p2MoveHistoryItem = moves.get(moves.size() - 2);
            FixSpikeMoveResult p1MoveResult = p1MoveHistoryItem.getMoveResult();
            FixSpikeMoveResult p2MoveResult = p2MoveHistoryItem.getMoveResult();
            int res = Integer.compare(p1MoveResult.getFixes(), p2MoveResult.getFixes());
            if (res == 0) {
                return -1L;
            }
            return res < 0 ? p2MoveHistoryItem.getPlayerId() : p1MoveHistoryItem.getPlayerId();
        }
        return null;
    }

    public String retrieveGuestToken() {
        final FixSpikePlayer guest = getGuest();
        return guest == null ? null : guest.getToken();
    }

    public String getOpponentsName(String token) {
        FixSpikePlayer player = secretHolders.get(token);
        return player != null ? player.getName() : null;
    }

    public FixSpikeStatusEventType calculateEventType(String token) {
        if (isGameOver()) return FixSpikeStatusEventType.GAME_OVER_EVT;
        if (hasMove(token)) return FixSpikeStatusEventType.MAKE_MOVE_EVT;
        return FixSpikeStatusEventType.AWAIT_RESTART_EVT;
    }

    public FixSpikeGameManager newManager(String token, String playerSecret) {
        final FixSpikePlayer host = getHost();
        final boolean currentHostRequestsRestart = token.equals(host.getToken());
        FixSpikePlayer newHost = currentHostRequestsRestart ? host.newSecret(playerSecret) : getGuest().newSecret(playerSecret);
        FixSpikePlayer newGuest = currentHostRequestsRestart? getGuest(): host;
        final FixSpikeGameManager newManager = FixSpikeGameManager.newGame(newHost);
        newManager.players[GUEST_IDX] = newGuest;
        return newManager;
    }

    public void addGuestWithNewSecret(String token, String secret) {
        final FixSpikePlayer playerByToken = getPlayerByToken(token);
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

    public String provideOpponentsSecret(String token) {
        FixSpikePlayer guest = getGuest();
        if(guest.getToken().equals(token)) {
            return new String(getHost().getSecret());
        }
        return new String(guest.getSecret());
    }
}
