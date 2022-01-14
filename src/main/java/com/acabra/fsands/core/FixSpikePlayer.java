package com.acabra.fsands.core;

import lombok.Getter;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class FixSpikePlayer {
    private final String name;
    private final char[] secretArr;
    private final String secret;
    private final String token;
    private final AtomicInteger movesCounter;
    private final long id;

    public FixSpikePlayer(long id, String name, String secret, String token) {
        this.id = id;
        this.name = name;
        this.secret = secret;
        this.secretArr = secret.toCharArray();
        this.token = token;
        this.movesCounter = new AtomicInteger(0);
    }

    public FixSpikeMoveResult respond(long moveId, FixSpikePlayer guesser, char[] guess) {
        ArrayList<Character> sGuess = new ArrayList<>();
        ArrayList<Character> sSecret = new ArrayList<>();
        int fixes = 0;
        for (int i = 0; i < guess.length; i++) {
            if(guess[i] != secretArr[i]) {
                sGuess.add(guess[i]);
                sSecret.add(secretArr[i]);
            } else {
                ++fixes;
            }
        }
        int spikes = 0;
        for (Character character : sGuess) {
            if(sSecret.contains(character)) {
                sSecret.remove(character);
                ++spikes;
            }
        }
        return FixSpikeMoveResult.builder()
                .withId(moveId)
                .withIndex(guesser.move())
                .withFixes(fixes)
                .withSpikes(spikes)
                .withGuess(guess)
                .withPlayerName(guesser.getName())
                .build();
    }

    public int move() {
        return movesCounter.getAndIncrement();
    }

    public FixSpikePlayer newSecret(String newSecret) {
        return new FixSpikePlayer(id, name, newSecret, token);
    }

    public int getIndex() {
        return movesCounter.get();
    }
}
