package com.acabra.fsands.auth;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ConstantTimePasswordChecker {
    static final long BASE_NANOS = TimeUnit.NANOSECONDS.convert(200, TimeUnit.MILLISECONDS);
    public static boolean check(String str1, String str2) {
        long now = System.nanoTime();
        CompletableFuture<Boolean> result = new CompletableFuture<>();
        TimedComparison timeCheck = TimedComparison.of(now, str1, str2);
        long pendingTime = Math.abs(BASE_NANOS - timeCheck.time);
        CompletableFuture.supplyAsync(() -> result.complete(timeCheck.res),
                CompletableFuture.delayedExecutor(pendingTime, TimeUnit.NANOSECONDS));
        return result.join();
    }

    static private class TimedComparison {

        private final boolean res;
        private final long time;

        public TimedComparison(long now, String str1, String str2) {
            res = str1.compareTo(str2) == 0;
            time = System.nanoTime() - now;
        }

        public static TimedComparison of(long now, String str1, String str2) {
            return new TimedComparison(now, str1, str2);
        }
    }
}
