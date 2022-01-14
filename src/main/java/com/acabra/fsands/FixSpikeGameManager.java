package com.acabra.fsands;

import com.acabra.fsands.core.*;
import com.acabra.fsands.request.FixSpikeRequestDTO;
import com.acabra.fsands.response.FixSpikeMoveResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class FixSpikeGameManager {

    public static final int HOST_IDX = 0;
    private static final int GUEST_IDX = 1;
    private final Map<String, FixSpikePlayer> secretHolders;
    private final FixSpikePlayer[] players;
    private volatile int currentTurn = Turn.NONE;
    private final ArrayList<FixSpikeHistoryEvent> eventHistory;
    private final AtomicInteger movesRegistry;
    private final AtomicReference<FixSpikeGameState> cache = new AtomicReference<>();
    private volatile boolean gameOver;

    private FixSpikeGameManager(FixSpikePlayer host) {
        this.players = new FixSpikePlayer[2];
        this.secretHolders = new HashMap<>();
        this.players[HOST_IDX] = host;
        this.eventHistory = new ArrayList<>() {{
            add(FixSpikeHistoryEvent.playerJoin(host));
        }};
        this.movesRegistry = new AtomicInteger(0);
        this.gameOver = false;
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
            return Turn.GUEST;
        }
        return Turn.HOST;
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
        FixSpikePlayer guesser = secretHolders.get(secretHolder.getToken());
        FixSpikeMoveResult moveResult = secretHolder.respond(movesRegistry.getAndIncrement(), guesser, guess);
        currentTurn = nextTurn(requestDTO.getToken());
        FixSpikeHistoryEvent latestEvent = FixSpikeHistoryEvent.playerMove(guesser, moveResult);
        if (isGameOver(moveResult)) {
            gameOver = true;
        }
        eventHistory.add(latestEvent);
        return FixSpikeMoveResponse.ok(responseId, gameOver, FixSpikeResultMapper.toMoveResultDTO(moveResult));
    }

    boolean lastMoveExit() {
        return findLastEvent().getEventType() == FixSpikeMoveEventType.EXIT;
    }

    private boolean isGameOver(FixSpikeMoveResult latestMoveResult) {
        if (eventHistory.isEmpty()) return false;
        FixSpikeMoveResult lastMove = findLastEvent().getMoveResult();
        return lastMove != null && isGameOver(latestMoveResult, lastMove);
    }

    public boolean hostWaitingForGuest() {
        FixSpikeHistoryEvent lastEvent = findLastEvent();
        return lastEvent.getEventType() == FixSpikeMoveEventType.JOIN
                && lastEvent.getPlayerId() == getHost().getId();
    }

    public void addGuest(FixSpikePlayer guest) {
        players[GUEST_IDX] = guest;
        FixSpikePlayer host = getHost();
        secretHolders.put(guest.getToken(), host);
        secretHolders.put(host.getToken(), guest);
        eventHistory.add(FixSpikeHistoryEvent.playerJoin(guest));
        currentTurn = Turn.HOST;
    }

    public String retrieveHostToken() {
        return getHost().getToken();
    }

    public boolean hasTurn(String token) {
        if (currentTurn == Turn.NONE) {
            return false;
        }
        if (currentTurn == Turn.HOST && getHost().getToken().equals(token)) {
            return true;
        }
        return currentTurn == Turn.GUEST && token.equals(getGuest().getToken());
    }

    public long provideEndResult() {
        final FixSpikeHistoryEvent p1MoveHistoryItem = findLastEvent();
        final FixSpikeHistoryEvent p2MoveHistoryItem = eventHistory.get(eventHistory.size() - 2);
        FixSpikeMoveResult p1MoveResult = p1MoveHistoryItem.getMoveResult();
        FixSpikeMoveResult p2MoveResult = p2MoveHistoryItem.getMoveResult();
        int res = Integer.compare(p1MoveResult.getFixes(), p2MoveResult.getFixes());
        if (res == 0) {
            return -1;
        }
        return res < 0 ? p2MoveHistoryItem.getPlayerId() : p1MoveHistoryItem.getPlayerId();
    }

    public String retrieveGuestToken() {
        final FixSpikePlayer guest = getGuest();
        return guest == null ? null : guest.getToken();
    }

    public FixSpikePlayer getOpponent(String token) {
        return secretHolders.get(token);
    }

    private FixSpikeGameStatus calculateGameStatus(String token, String opponentSecret) {
        if (gameOver) {
            return FixSpikeGameStatus.gameOver(provideEndResult(), opponentSecret);
        }
        if (hasTurn(token)) return FixSpikeGameStatus.makeMove();
        return FixSpikeGameStatus.awaitMove();
    }

    public FixSpikeGameManager newManager(String token, String playerSecret) {
        final FixSpikePlayer host = getHost();
        final boolean currentHostRequestsRestart = token.equals(host.getToken());
        FixSpikePlayer newHost = currentHostRequestsRestart ? host.newSecret(playerSecret) : getGuest().newSecret(playerSecret);
        FixSpikePlayer newGuest = currentHostRequestsRestart ? getGuest() : host;
        final FixSpikeGameManager newManager = FixSpikeGameManager.newGame(newHost);
        newManager.players[GUEST_IDX] = newGuest;
        return newManager;
    }

    public void addGuestWithNewSecret(String token, String secret) {
        if (players[GUEST_IDX] == null) return;
        final FixSpikePlayer playerByToken = getPlayerByToken(token);
        if (!playerByToken.getToken().equals(getGuest().getToken())) {
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

    public FixSpikeGameState provideGameState(Long sequence, String token) {
        FixSpikeGameState cached = cache.get();
        if (sequence != null && cached != null && cached.getSequenceId() == sequence) {
            return cached;
        }
        logger.info("cache missed");
        FixSpikePlayer opponent = getOpponent(token);
        if (opponent == null) {
            if (lastMoveExit()) { //request game restart
                return FixSpikeGameState.builder()
                        .withGameStatus(FixSpikeGameStatus.requestRestart())
                        .build();
            }
            FixSpikeGameState notStarted = FixSpikeGameState.builder()
                    .withGameStatus(FixSpikeGameStatus.notStarted())
                    .build();
            cache.set(notStarted);
            return notStarted;
        }
        FixSpikeHistoryEvent lastHistoryItem = findLastEvent();
        FixSpikeGameStatus gameStatus = calculateGameStatus(token, opponent.getSecret());
        Boolean isOwnMove = lastHistoryItem != null ? token.equals(lastHistoryItem.getPlayerToken()) : null;
        FixSpikeGameState gameState = FixSpikeGameState.builder()
                .withGameStatus(gameStatus)
                .withOpponentName(opponent.getName())
                .withIsOwnMove(isOwnMove)
                .withLastMove(lastHistoryItem != null ? lastHistoryItem.getMoveResult() : null)
                .build();
        cache.set(gameState);
        return gameState;
    }

    private FixSpikeHistoryEvent findLastEvent() {
        return eventHistory.get(eventHistory.size() - 1);
    }

    public void playerExit(String token) {
        logger.info("--------------------------------------------------------------");
        cache.set(null);
        FixSpikePlayer host = secretHolders.get(token);
        eventHistory.add(FixSpikeHistoryEvent.playerLeft(secretHolders.get(host.getToken())));
        secretHolders.clear();
        players[HOST_IDX] = host;
        players[GUEST_IDX] = null;
        logger.info("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean gameHasNotStarted() {
        return eventHistory.stream().noneMatch(e -> e.getEventType() == FixSpikeMoveEventType.MOVE);
    }
}
