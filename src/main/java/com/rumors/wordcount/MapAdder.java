package com.rumors.wordcount;

import java.util.Map;

public class MapAdder {

    public void add(Map<String, Integer> shorter, Map<String, Integer> total) {
        shorter.forEach((word, count) -> total.merge(word, count, Integer::sum));
    }
}
