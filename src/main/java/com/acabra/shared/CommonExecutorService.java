package com.acabra.shared;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CommonExecutorService implements AutoCloseable{

    private final ScheduledExecutorService ex;

    public CommonExecutorService() {
        this.ex = Executors.newSingleThreadScheduledExecutor();
    }

    public void scheduleAtFixedRate(Runnable task, int initialDelay, int period, TimeUnit unit) {
        this.ex.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    @Override
    public void close() throws Exception {
        if (!ex.isShutdown() && !ex.awaitTermination(1, TimeUnit.SECONDS)) {
            ex.shutdownNow();
        }
    }
}
