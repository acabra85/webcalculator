package com.acabra.roulette;

import com.acabra.roulette.response.RouletteConfigResponse;
import com.acabra.roulette.response.RouletteResponse;
import com.acabra.calculator.response.SimpleResponse;
import jdk.nashorn.internal.objects.NativeRegExp;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class RouletteManager {
    final static int MAX_HISTORY_SIZE = 300;
    private static final Comparator<? super Map.Entry<Integer, Integer>> COMP = (a, b) -> Integer.compare(b.getValue(), a.getValue());
    private final Long id;
    private final int MAX_HOT_NUMBERS = 4;
    private final int MAX_COLD_NUMBERS = 4;
    private final int MAX_HISTORY_DISPLAY = 25;

    private final Predicate<PriorityQueue<Map.Entry<Integer, Integer>>> HAS_HOT_NUMBERS = pq -> pq.size() > 0 && pq.peek().getValue() >= 3;

    private ArrayDeque<Integer> history = new ArrayDeque<>(MAX_HISTORY_SIZE);
    private final ArrayDeque<Integer> historyDisplay = new ArrayDeque<>(MAX_HISTORY_DISPLAY);
    private AtomicInteger counter = new AtomicInteger();
    private final Map<Integer, Integer> frequency = new HashMap<Integer, Integer>() {{
        for (int i = 0; i <= 36; ++i) put(i, 0);
    }};

    private final RouletteStats rouletteStats =  new RouletteStats();

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

    private final AtomicReference<Long> lastAccessed;

    public RouletteManager(long id) {
        this.id = id;
        this.lastAccessed = new AtomicReference<Long>() {{
            this.set(System.currentTimeMillis());
        }};
    }

    private SimpleResponse buildResponse() {
        PriorityQueue<Map.Entry<Integer, Integer>> pq = new PriorityQueue<>(COMP);
        pq.addAll(frequency.entrySet());
        ArrayList<Integer> hotNums = new ArrayList<Integer>() {{
            for (int i = 0; i < MAX_HOT_NUMBERS && i < history.size() && HAS_HOT_NUMBERS.test(pq) ; ++i) {
                add(pq.remove().getKey());
            }
        }};
        while (pq.size() > MAX_COLD_NUMBERS) pq.remove();
        ArrayList<Integer> history = new ArrayList<>(historyDisplay);
        ArrayList<Integer> coldNumbers = new ArrayList<Integer>() {{
            while (!pq.isEmpty()) add(pq.remove().getKey());
        }};

        return new RouletteResponse(counter.getAndIncrement(), false, hotNums, coldNumbers, history, rouletteStats);
    }

    public SimpleResponse addResult(Integer result) {
        synchronized (lastAccessed) {
            lastAccessed.set(System.currentTimeMillis());
        }
        if (history.size() == MAX_HISTORY_SIZE) {
            Integer removed = history.removeFirst();
            frequency.put(removed, Math.max(frequency.get(removed) - 1, 0));
        }
        if (historyDisplay.size() == MAX_HISTORY_DISPLAY) {
            historyDisplay.removeFirst();
        }
        history.addLast(result);
        historyDisplay.addLast(result);
        frequency.put(result, frequency.get(result) + 1);
        rouletteStats.accept(result, NUMBER_COLORS.get(result));
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
