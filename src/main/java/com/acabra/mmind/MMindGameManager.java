package com.acabra.mmind;

import com.acabra.calculator.response.SimpleResponse;
import com.acabra.mmind.core.MMindHistoryItem;
import com.acabra.mmind.core.MMindPlayer;
import com.acabra.mmind.core.MMmindMoveResult;
import com.acabra.mmind.core.Moves;
import com.acabra.mmind.request.MMindJoinRoomRequestDTO;
import com.acabra.mmind.request.MMindRequestDTO;
import com.acabra.mmind.response.MMindMoveResponse;
import com.acabra.mmind.response.MMindTokenInfoDTO;

import java.util.*;

public class MMindGameManager {

    private final Map<String, MMindPlayer> players;
    private final Map<String, MMindPlayer> secretHolders;
    private final MMindPlayer host;
    private volatile int currentMove = Moves.NONE;
    private final ArrayList<MMindHistoryItem> moves;

    private MMindGameManager(MMindPlayer host) {
        this.host = host;
        players = new HashMap<>();
        secretHolders = new HashMap<>();
        players.put(host.getToken(), host);
        moves = new ArrayList<>();
    }

    public static MMindGameManager newGame(MMindPlayer host) {
        return new MMindGameManager(host);
    }

    public SimpleResponse attemptMove(long id, MMindRequestDTO requestDTO) {
        char[] guess = requestDTO.getGuess().toCharArray();
        MMindPlayer secretHolder = secretHolders.get(requestDTO.getToken());
        MMindPlayer guesser = players.get(requestDTO.getToken());
        MMmindMoveResult moveResult = secretHolder.respond(guesser.move(), guess);
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
        return MMindMoveResponse.ok(id, isGameOver(), MMindResultMapper.toResultDTO(moveResult));
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
        if(currentMove == 1 && host.getToken().equals(token)) {
            return true;
        }
        return currentMove == 2 && token.equals(getGuest().getToken());
    }

    public MMmindMoveResult getLastMove() {
        return moves.isEmpty() ? null : moves.get(moves.size() - 1).getMoveResult();
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
        if(moves.size() < 2) {
            throw new IllegalStateException("Unable to provide a result for a game in progress");
        }
        MMmindMoveResult p1MoveResult = moves.get(moves.size()-1).getMoveResult();
        MMmindMoveResult p2MoveResult = moves.get(moves.size()-2).getMoveResult();
        if(isGameOver(p1MoveResult, p2MoveResult)) {
            int res = Integer.compare(p1MoveResult.getFixes(), p2MoveResult.getFixes());
            if(res == 0) {
                return "Tie";
            }
            return res < 0 ? p2MoveResult.getPlayerName() : p1MoveResult.getPlayerName();
        }
        return null;
    }
}
