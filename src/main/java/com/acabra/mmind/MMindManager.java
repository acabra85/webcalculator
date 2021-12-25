package com.acabra.mmind;

import com.acabra.calculator.response.SimpleResponse;
import com.acabra.mmind.core.MMindHistoryItem;
import com.acabra.mmind.core.MMindPlayer;
import com.acabra.mmind.core.MMmindMoveResult;
import com.acabra.mmind.core.Moves;
import com.acabra.mmind.request.MMindRequestDTO;
import com.acabra.mmind.response.MMindMoveResponse;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MMindManager {

    private final Map<String, MMindPlayer> players;
    private final Map<String, MMindPlayer> secretHolders;
    private final MMindPlayer host;
    private volatile int currentMove = Moves.NONE;
    private Stack<MMindHistoryItem> moves;

    private MMindManager(MMindPlayer host) {
        this.host = host;
        players = new HashMap<>();
        secretHolders = new HashMap<>();
        players.put(host.getToken(), host);
        moves = new Stack<>();
    }

    public static MMindManager newGame(MMindPlayer host) {
        return new MMindManager(host);
    }

    public SimpleResponse executeMove(MMindRequestDTO requestDTO) {
        char[] guess = requestDTO.getGuess().toCharArray();
        MMindPlayer secretHolder = secretHolders.get(requestDTO.getToken());
        MMmindMoveResult moveResult = secretHolder.respond(guess);
        if(host.getToken().equals(requestDTO.getToken())) {
            currentMove = Moves.GUEST;
        } else {
            currentMove = Moves.HOST;
        }
        moves.push(MMindHistoryItem.builder()
                .withMoveResult(moveResult)
                .withPlayerToken(requestDTO.getToken())
                .build());
        return new MMindMoveResponse(0L, false, MMindResultMapper.toResultDTO(moveResult));
    }

    public boolean awaitingPlayer() {
        return players.size() < 2;
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
        if(host.getToken().equals(token) && currentMove == 1) {
            return true;
        }
        return currentMove == 2 && token.equals(secretHolders.get(host.getToken()).getToken());
    }

    public MMmindMoveResult getLastMove() {
        return moves.isEmpty() ? null : moves.peek().getMoveResult();
    }
}
