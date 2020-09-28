package com.acabra.roulette;

import com.acabra.roulette.response.RouletteConfigResponse;
import com.acabra.roulette.response.RouletteResponse;
import com.acabra.calculator.response.SimpleResponse;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class RouletteManager {
    final static int MAX_SIZE = 25;
    private final Long id;

    private ArrayDeque<Integer> history25 = new ArrayDeque<>(25);
    private List<Integer> coldNumbers = new LinkedList<>();
    private List<Integer> hotNumbers = new LinkedList<>();
    private AtomicInteger counter = new AtomicInteger();

    private static final Map<Integer, Integer> NUMBER_COLORS = new HashMap<Integer, Integer>() {{
        put(0, RouletteColor.GREEN.getId());
        put(1, RouletteColor.RED.getId());
        put(3, RouletteColor.RED.getId());
        put(5, RouletteColor.RED.getId());
        put(7, RouletteColor.RED.getId());
        put(9, RouletteColor.RED.getId());
        put(12, RouletteColor.RED.getId());
        put(14, RouletteColor.RED.getId());
        put(16, RouletteColor.RED.getId());
        put(18, RouletteColor.RED.getId());
        put(19, RouletteColor.RED.getId());
        put(21, RouletteColor.RED.getId());
        put(23, RouletteColor.RED.getId());
        put(25, RouletteColor.RED.getId());
        put(27, RouletteColor.RED.getId());
        put(30, RouletteColor.RED.getId());
        put(32, RouletteColor.RED.getId());
        put(34, RouletteColor.RED.getId());
        put(36, RouletteColor.RED.getId());
        put(2, RouletteColor.BLACK.getId());
        put(4, RouletteColor.BLACK.getId());
        put(6, RouletteColor.BLACK.getId());
        put(8, RouletteColor.BLACK.getId());
        put(10, RouletteColor.BLACK.getId());
        put(11, RouletteColor.BLACK.getId());
        put(13, RouletteColor.BLACK.getId());
        put(15, RouletteColor.BLACK.getId());
        put(17, RouletteColor.BLACK.getId());
        put(20, RouletteColor.BLACK.getId());
        put(22, RouletteColor.BLACK.getId());
        put(24, RouletteColor.BLACK.getId());
        put(26, RouletteColor.BLACK.getId());
        put(28, RouletteColor.BLACK.getId());
        put(29, RouletteColor.BLACK.getId());
        put(31, RouletteColor.BLACK.getId());
        put(33, RouletteColor.BLACK.getId());
        put(35, RouletteColor.BLACK.getId());
    }};

    private AtomicReference<Long> lastAccessed;

    public RouletteManager(long id) {
        this.id = id;
        this.lastAccessed = new AtomicReference<Long>(){{this.set(System.currentTimeMillis());}};
    }

    private SimpleResponse buildResponse() {
        return new RouletteResponse(counter.getAndIncrement(), false, hotNumbers, coldNumbers, new ArrayList<>(history25));
    }

    public SimpleResponse addResult(Integer result) {
        synchronized (lastAccessed) {
            lastAccessed.set(System.currentTimeMillis());
        }
        if(history25.size() == MAX_SIZE) {
            history25.removeFirst();
        }
        history25.addLast(result);
        return buildResponse();
    }

    public SimpleResponse getConfig() {
        return new RouletteConfigResponse(counter.getAndIncrement(), false, id.toString(),
                Collections.unmodifiableMap(new HashMap<>(RouletteManager.NUMBER_COLORS)));
    }

    public SimpleResponse spinRoulette() {
        return addResult(new SecureRandom().nextInt(NUMBER_COLORS.size()));
    }

    synchronized public long getLastAccessed() {
        return lastAccessed.get();
    }

    public long getId() {
        return this.id;
    }
}
