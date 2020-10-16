package com.acabra.roulette;

import com.acabra.calculator.response.SimpleResponse;
import com.acabra.roulette.response.RouletteConfigResponse;
import com.acabra.roulette.response.RouletteResponse;
import com.acabra.roulette.stats.RouletteStats;
import com.acabra.roulette.stats.RouletteStatsFull;
import com.acabra.roulette.stats.RouletteStatsWindow;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
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

    private final RouletteStats rouletteStatsFull = new RouletteStatsFull();
    private final RouletteStatsWindow rouletteStats25 = new RouletteStatsWindow(TEMPERATURE_WINDOW_SIZE);

    private static final int[] NUMBER_COLORS = buildNumberIndexColors();

    private final AtomicReference<Long> lastAccessed;
    private final Executor ex;
    private final ArrayDeque<Integer> history25 = new ArrayDeque<>();

    public RouletteManager(long id, Executor ex) {
        this.id = id;
        this.lastAccessed = new AtomicReference<Long>() {{
            this.set(System.currentTimeMillis());
        }};
        this.ex = ex;
    }

    private static int[] buildNumberIndexColors() {
        int[] numberColors = new int[ROULETTE_SIZE];
        numberColors[0] = RouletteColor.GREEN.getId();
        numberColors[1] = RouletteColor.RED.getId();
        numberColors[3] = RouletteColor.RED.getId();
        numberColors[5] = RouletteColor.RED.getId();
        numberColors[7] = RouletteColor.RED.getId();
        numberColors[9] = RouletteColor.RED.getId();
        numberColors[12] = RouletteColor.RED.getId();
        numberColors[14] = RouletteColor.RED.getId();
        numberColors[16] = RouletteColor.RED.getId();
        numberColors[18] = RouletteColor.RED.getId();
        numberColors[19] = RouletteColor.RED.getId();
        numberColors[21] = RouletteColor.RED.getId();
        numberColors[23] = RouletteColor.RED.getId();
        numberColors[25] = RouletteColor.RED.getId();
        numberColors[27] = RouletteColor.RED.getId();
        numberColors[30] = RouletteColor.RED.getId();
        numberColors[32] = RouletteColor.RED.getId();
        numberColors[34] = RouletteColor.RED.getId();
        numberColors[36] = RouletteColor.RED.getId();
        numberColors[2] = RouletteColor.BLACK.getId();
        numberColors[4] = RouletteColor.BLACK.getId();
        numberColors[6] = RouletteColor.BLACK.getId();
        numberColors[8] = RouletteColor.BLACK.getId();
        numberColors[10] = RouletteColor.BLACK.getId();
        numberColors[11] = RouletteColor.BLACK.getId();
        numberColors[13] = RouletteColor.BLACK.getId();
        numberColors[15] = RouletteColor.BLACK.getId();
        numberColors[17] = RouletteColor.BLACK.getId();
        numberColors[20] = RouletteColor.BLACK.getId();
        numberColors[22] = RouletteColor.BLACK.getId();
        numberColors[24] = RouletteColor.BLACK.getId();
        numberColors[26] = RouletteColor.BLACK.getId();
        numberColors[28] = RouletteColor.BLACK.getId();
        numberColors[29] = RouletteColor.BLACK.getId();
        numberColors[31] = RouletteColor.BLACK.getId();
        numberColors[33] = RouletteColor.BLACK.getId();
        numberColors[35] = RouletteColor.BLACK.getId();
        return numberColors;
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
        return new RouletteResponse(counter.getAndIncrement(), false, hotNums, coldNumbers, history.getLast(),
                rouletteStatsFull.toDto(), rouletteStats25.toDto());
    }

    public SimpleResponse addResult(Integer result) {
        lastAccessed.set(System.currentTimeMillis());
        if (history25.size() >= TEMPERATURE_WINDOW_SIZE) {
            int first = history25.removeFirst();
            if (rouletteStats25.isFull()) {
                rouletteStats25.decreaseCountersByOne(first, NUMBER_COLORS[first]);
            }
            --frequency[first];
        }
        if (history.size() == MAX_HISTORY_SIZE) {
            history.removeFirst();
        }
        history.addLast(result);
        history25.addLast(result);
        ++frequency[result];
        rouletteStatsFull.accept(result, NUMBER_COLORS[result]);
        rouletteStats25.accept(result, NUMBER_COLORS[result]);
        return buildResponse();
    }

    public SimpleResponse getConfig() {
        return new RouletteConfigResponse(counter.getAndIncrement(), false, id.toString(), NUMBER_COLORS);
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
