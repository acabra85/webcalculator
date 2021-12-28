package com.acabra.roulette;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Slf4j
public class RandomCalculator implements Runnable {


    private final int rouletteSize;
    private final CompletableFuture<Integer> future = new CompletableFuture<>();

    public RandomCalculator(int rouletteSize) {
        this.rouletteSize = rouletteSize;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(new Random().nextInt(100));
            future.complete(new SecureRandom().nextInt(rouletteSize));
        } catch (InterruptedException ie) {
            logger.error("ie ", ie);
            future.completeExceptionally(ie);
        }
    }

    public CompletableFuture<Integer> getResponse() {
        return future;
    }
}
