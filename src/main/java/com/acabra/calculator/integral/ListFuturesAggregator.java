package com.acabra.calculator.integral;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Created by Agustin on 10/6/2016.
 */
public class ListFuturesAggregator {

    /**
     * Transforms a list of futures to a future containing a list of doubles
     * @param futures the list of futures to aggregate
     * @return a future representing the list of doubles to aggregate.
     */
    public static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        if (!futures.isEmpty()) {
            CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
            return allDoneFuture.thenApply(v -> futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList()
                    )
            );
        }
        return CompletableFuture.completedFuture(Collections.emptyList());
    }
}
