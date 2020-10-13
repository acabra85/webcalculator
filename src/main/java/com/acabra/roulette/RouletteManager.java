package com.acabra.roulette;

import com.acabra.calculator.response.SimpleResponse;
import com.acabra.roulette.response.RouletteConfigResponse;
import com.acabra.roulette.response.RouletteResponse;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class RouletteManager {
    private static final Logger logger = Logger.getLogger(RouletteManager.class);

    final static int MAX_HISTORY_SIZE = 300;
    private static final Comparator<? super Map.Entry<Integer, Integer>> COMP = (a, b) -> Integer.compare(b.getValue(), a.getValue());
    private static final int ROULETTE_SIZE = 37;
    private static final int TEMPERATURE_WINDOW_SIZE = 25;
    private final Long id;
    private final int MAX_HOT_NUMBERS = 4;
    private final int MAX_COLD_NUMBERS = 4;

    private final Predicate<PriorityQueue<Map.Entry<Integer, Integer>>> HAS_HOT_NUMBERS = pq -> pq.size() > 0 && pq.peek().getValue() >= 3;

    private ArrayDeque<Integer> history = new ArrayDeque<>(MAX_HISTORY_SIZE);
    private AtomicInteger counter = new AtomicInteger();
    private final int[] frequency = new int[ROULETTE_SIZE];

    private final RouletteStats rouletteStats =  new RouletteStats();

    private static final int[] NUMBER_COLORS = new int[ROULETTE_SIZE];

    static {
        NUMBER_COLORS[0] = RouletteColor.GREEN.getId();
        NUMBER_COLORS[1] = RouletteColor.RED.getId();
        NUMBER_COLORS[3] = RouletteColor.RED.getId();
        NUMBER_COLORS[5] = RouletteColor.RED.getId();
        NUMBER_COLORS[7] = RouletteColor.RED.getId();
        NUMBER_COLORS[9] = RouletteColor.RED.getId();
        NUMBER_COLORS[12] = RouletteColor.RED.getId();
        NUMBER_COLORS[14] = RouletteColor.RED.getId();
        NUMBER_COLORS[16] = RouletteColor.RED.getId();
        NUMBER_COLORS[18] = RouletteColor.RED.getId();
        NUMBER_COLORS[19] = RouletteColor.RED.getId();
        NUMBER_COLORS[21] = RouletteColor.RED.getId();
        NUMBER_COLORS[23] = RouletteColor.RED.getId();
        NUMBER_COLORS[25] = RouletteColor.RED.getId();
        NUMBER_COLORS[27] = RouletteColor.RED.getId();
        NUMBER_COLORS[30] = RouletteColor.RED.getId();
        NUMBER_COLORS[32] = RouletteColor.RED.getId();
        NUMBER_COLORS[34] = RouletteColor.RED.getId();
        NUMBER_COLORS[36] = RouletteColor.RED.getId();
        NUMBER_COLORS[2] = RouletteColor.BLACK.getId();
        NUMBER_COLORS[4] = RouletteColor.BLACK.getId();
        NUMBER_COLORS[6] = RouletteColor.BLACK.getId();
        NUMBER_COLORS[8] = RouletteColor.BLACK.getId();
        NUMBER_COLORS[10] = RouletteColor.BLACK.getId();
        NUMBER_COLORS[11] = RouletteColor.BLACK.getId();
        NUMBER_COLORS[13] = RouletteColor.BLACK.getId();
        NUMBER_COLORS[15] = RouletteColor.BLACK.getId();
        NUMBER_COLORS[17] = RouletteColor.BLACK.getId();
        NUMBER_COLORS[20] = RouletteColor.BLACK.getId();
        NUMBER_COLORS[22] = RouletteColor.BLACK.getId();
        NUMBER_COLORS[24] = RouletteColor.BLACK.getId();
        NUMBER_COLORS[26] = RouletteColor.BLACK.getId();
        NUMBER_COLORS[28] = RouletteColor.BLACK.getId();
        NUMBER_COLORS[29] = RouletteColor.BLACK.getId();
        NUMBER_COLORS[31] = RouletteColor.BLACK.getId();
        NUMBER_COLORS[33] = RouletteColor.BLACK.getId();
        NUMBER_COLORS[35] = RouletteColor.BLACK.getId();
    }

    private final AtomicReference<Long> lastAccessed;
    private final Executor ex;

    public RouletteManager(long id, Executor ex) {
        this.id = id;
        this.lastAccessed = new AtomicReference<Long>() {{
            this.set(System.currentTimeMillis());
        }};
        this.ex = ex;
    }

    private SimpleResponse buildResponse() {
        PriorityQueue<Map.Entry<Integer, Integer>> pq = new PriorityQueue<>(COMP);
        IntStream.range(0, ROULETTE_SIZE).forEach(i-> pq.add(new AbstractMap.SimpleEntry<>(i, frequency[i])));
        ArrayList<Integer> hotNums = new ArrayList<Integer>() {{
            for (int i = 0; i < MAX_HOT_NUMBERS && i < history.size() && HAS_HOT_NUMBERS.test(pq) ; ++i) {
                add(pq.remove().getKey());
            }
        }};
        while (pq.size() > MAX_COLD_NUMBERS) pq.remove();
        ArrayList<Integer> coldNumbers = new ArrayList<Integer>() {{
            while (!pq.isEmpty()) add(pq.remove().getKey());
        }};
        return new RouletteResponse(counter.getAndIncrement(), false, hotNums, coldNumbers, history.getLast(), rouletteStats);
    }

    public SimpleResponse addResult(Integer result) {
        synchronized (lastAccessed) {
            lastAccessed.set(System.currentTimeMillis());
        }
        if (history.size() > TEMPERATURE_WINDOW_SIZE) {
            --frequency[history.getFirst()];
        }
        if (history.size() == MAX_HISTORY_SIZE) {
            history.removeFirst();
        }
        history.addLast(result);
        ++frequency[result];
        rouletteStats.accept(result, NUMBER_COLORS[result]);
        return buildResponse();
    }

    public SimpleResponse getConfig() {
        return new RouletteConfigResponse(counter.getAndIncrement(), false, id.toString(), NUMBER_COLORS.clone());
    }

    public SimpleResponse spinRoulette() {
        RandomCalculator randomCalculator = new RandomCalculator(ROULETTE_SIZE);
        CompletableFuture.runAsync(randomCalculator, ex);
        Integer response = -1;
        try {
            response = randomCalculator.getResponse().get();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("unable to retrieve response from random", e);
        }
        return addResult(response);
    }

    synchronized public long getLastAccessed() {
        return lastAccessed.get();
    }

    public long getId() {
        return this.id;
    }
}
