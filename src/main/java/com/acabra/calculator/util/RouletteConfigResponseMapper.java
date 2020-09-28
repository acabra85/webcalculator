package com.acabra.calculator.util;

import java.util.Iterator;
import java.util.Map;
import java.util.stream.IntStream;

public class RouletteConfigResponseMapper {
    public static String toJsonMap(Map<Integer, Integer> configMap) {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<Integer, Integer>> iterator = configMap.entrySet().iterator();
        sb.append("[");
        IntStream.range(0, configMap.size()).forEach(i-> {
            Map.Entry<Integer, Integer> next = iterator.next();
            sb.append("[")
                .append(next.getKey()).append(",").append(next.getValue())
                .append("]").append(i < configMap.size() - 1 ? "," : "");
        });
        sb.append("]");
        return sb.toString();
    }
}
