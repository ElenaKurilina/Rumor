package com.rumors.listener;

import com.rumors.Measure;
import com.rumors.web.SearchEngine;
import com.rumors.web.WebPageReader;
import com.rumors.wordcount.MapAdder;
import com.rumors.wordcount.TopCollector;
import com.rumors.wordcount.WordCounter;
import io.micrometer.core.instrument.LongTaskTimer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class RumorListener {

    private final SearchEngine engine;
    private final WebPageReader pageReader;
    private final WordCounter counter;
    private final MapAdder mapAdder;

    private final int top;

    public RumorListener(SearchEngine engine, WebPageReader pageReader, WordCounter counter, MapAdder mapAdder, int top) {
        this.engine = engine;
        this.pageReader = pageReader;
        this.counter = counter;
        this.mapAdder = mapAdder;
        this.top = top;
    }

    public LinkedHashMap<String, Integer> listenToRumors(String topic) {
        LongTaskTimer.Sample timer = Measure.startTimer("rumor");

        Map<String, Integer> totalCount = new HashMap<>();
        Set<String> links = engine.getLinksFor(topic);
        for (String url : links) {
            String text = pageReader.read(url);
            String article = removeTextBeforeArticle(text);
            Map<String, Integer> count = counter.countWords(article, topic);
            mapAdder.add(count, totalCount);
        }
        LinkedHashMap<String, Integer> rumors = TopCollector.collectOrderedTop(totalCount, top);

        timer.stop();
        return rumors;
    }

    private static String removeTextBeforeArticle(String text) {
        if (text.trim().length() > 500) {
            return text.substring(100);
        }
        return text;
    }

}
