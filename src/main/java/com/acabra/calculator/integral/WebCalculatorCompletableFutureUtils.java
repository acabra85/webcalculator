package com.acabra.calculator.integral;

import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Agustin on 10/6/2016.
 */
public class WebCalculatorCompletableFutureUtils {

    private static final Logger logger = Logger.getLogger(WebCalculatorCompletableFutureUtils.class);
    /**
     * Transforms a list of futures to a future containing a list of doubles
     * @param futures the list of futures to aggregate
     * @return a future representing the list of doubles to aggregate.
     */
    public static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        if (!futures.isEmpty()) {
            //CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
            .exceptionally(e -> {
                logger.error(e);
                throw new UnsupportedOperationException("unable to process request");
            })
            .thenApply(v -> futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList())
            );
        }
        return CompletableFuture.completedFuture(Collections.emptyList());
    }

    /**
     *
     * @param future the future handle
     * @param fallback  the function to execute (future, error) -> future
     * @param rFunction the function that transforms the future result into return object type
     * @param <T> The type of the completable future input
     * @param <R> The type of the completable future's return
     * @return CompletableFuture of type R
     */
    public static <T, R> CompletableFuture<R> withFallbackDifferentResponse(CompletableFuture<T> future,
                                     BiFunction<CompletableFuture<T>, Throwable, CompletableFuture<R>> fallback,
                                     Function<CompletableFuture<T>, CompletableFuture<R>> rFunction) {
        return future.handle((response, error) -> error)
                .thenCompose(error -> error!=null ? fallback.apply(future,error): rFunction.apply(future));
    }
}
