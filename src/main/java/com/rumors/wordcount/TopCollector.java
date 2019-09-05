package com.rumors.wordcount;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class TopCollector {

    private final static Comparator<Map.Entry<String, Integer>> countComparator =
            (o1, o2) -> Integer.compare(o2.getValue(), o1.getValue());

    public static LinkedHashMap<String, Integer> collectOrderedTop(Map<String, Integer> totalCount, int top) {
        LinkedHashMap<String, Integer> result = new LinkedHashMap<>();
        totalCount.entrySet().stream()
                .sorted(countComparator)
                .limit(top)
                .forEach(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }
}
