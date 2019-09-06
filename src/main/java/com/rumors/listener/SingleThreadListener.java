package com.rumors.listener;

import com.rumors.Measure;
import com.rumors.web.SearchEngine;
import com.rumors.web.WebPageReader;
import com.rumors.wordcount.MapAdder;
import com.rumors.wordcount.WordCounter;
import io.micrometer.core.instrument.LongTaskTimer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class SingleThreadListener implements Listener {

    private final SearchEngine searchEngine;
    private final WebPageReader pageReader;
    private final WordCounter counter;
    private final MapAdder mapAdder;

    public SingleThreadListener(SearchEngine searchEngine, WebPageReader pageReader, WordCounter counter, MapAdder mapAdder) {
        this.searchEngine = searchEngine;
        this.pageReader = pageReader;
        this.counter = counter;
        this.mapAdder = mapAdder;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void listenToRumors(String topic, Function<Map<String, Integer>, Void> callback) {
        LongTaskTimer.Sample timer = Measure.startTimer("rumor");

        Map<String, Integer> totalCount = new HashMap<>();
        Set<String> links = searchEngine.findLinksFor(topic);
        for (String link : links) {
            String text = pageReader.read(link);
            String article = removeTextBeforeArticle(text);
            Map<String, Integer> count = counter.countWords(article, topic);
            mapAdder.add(count, totalCount);
        }
        timer.stop();
        callback.apply(totalCount);
    }

    private static String removeTextBeforeArticle(String text) {
        if (text.trim().length() > 500) {
            return text.substring(100);
        }
        return text;
    }

}
