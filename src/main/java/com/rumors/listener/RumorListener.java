package com.rumors.listener;

import com.rumors.search.SearchEngine;
import com.rumors.search.WebPageReader;

import java.util.*;

public class RumorListener {

    private final SearchEngine engine;
    private final WebPageReader pageReader;
    private final WordCounter counter;

    private final static Comparator<Map.Entry<String, Integer>> countComparator =
            (o1, o2) -> Integer.compare(o2.getValue(), o1.getValue());
    private final int top;

    public RumorListener(SearchEngine engine, WebPageReader pageReader, WordCounter counter, int top) {
        this.engine = engine;
        this.pageReader = pageReader;
        this.counter = counter;
        this.top = top;
    }

    public LinkedHashMap<String, Integer> listenToRumors(String topic) {
        Map<String, Integer> totalCount = new HashMap<>();

        Set<String> links = engine.getLinksFor(topic);

        for (String url : links) {
            String article = removeTextBeforeArticle(pageReader.read(url));
            Map<String, Integer> count = counter.countWords(article, topic);
            add(count, totalCount);
        }

        return collectTopRumors(totalCount);
    }

    private LinkedHashMap<String, Integer> collectTopRumors(Map<String, Integer> totalCount) {
        LinkedHashMap<String, Integer> result = new LinkedHashMap<>();
        totalCount.entrySet().stream()
                .sorted(countComparator)
                .limit(top)
                .forEach(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    private static String removeTextBeforeArticle(String text) {
        if (text.trim().length() > 500) {
            return text.substring(100);
        }
        return text;
    }

    private void add(Map<String, Integer> shorter, Map<String, Integer> longer) {
        shorter.forEach((word, count) -> longer.merge(word, count, Integer::sum));
    }
}
