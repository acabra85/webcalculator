package com.acabra.mmind.core;

import lombok.Getter;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class MMindPlayer {
    final String name;
    final char[] secret;
    final String token;
    private final AtomicInteger movesCounter;

    public MMindPlayer(String name, String secret, String token) {
        this.name = name;
        this.secret = secret.toCharArray();
        this.token = token;
        this.movesCounter = new AtomicInteger(1);
    }

    public MMmindMoveResult respond(long moveId, int index, char[] guess) {
        ArrayList<Character> sGuess = new ArrayList<>();
        ArrayList<Character> sSecret = new ArrayList<>();
        int fixes = 0;
        for (int i = 0; i < guess.length; i++) {
            if(guess[i] != secret[i]) {
                sGuess.add(guess[i]);
                sSecret.add(secret[i]);
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
        return MMmindMoveResult.builder()
                .withId(moveId)
                .withIndex(index)
                .withFixes(fixes)
                .withSpikes(spikes)
                .withGuess(guess)
                .build();
    }

    public int move() {
        return movesCounter.getAndIncrement();
    }
}
