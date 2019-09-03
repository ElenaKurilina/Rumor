package com.rumors.search;

import com.rumors.Measure;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.LongTaskTimer;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class DuckDuckGo implements SearchEngine {

    private static final String LINKS_MAIN = "links_main";
    private static final String A = "a";
    private static final String HREF = "href";
    private static final String LINKS = "links";
    private static final String RESULTS_LINKS = "results_links";
    private final Logger logger = Logger.getLogger(SearchEngine.class);
    private final static String DUCKDUCKGO_SEARCH_URL = "https://duckduckgo.com/html/?q=";

    @Override
    public Set<String> getLinksFor(String query) {
        Document pageWithSearchResults;
        Counter failCounter = Measure.meterRegistry.counter("duckFail");
        LongTaskTimer.Sample timer = Measure.startTimer("duckSearch");
        try {
            pageWithSearchResults = Jsoup.connect(DUCKDUCKGO_SEARCH_URL + query).get();
        } catch (IOException e) {
            logger.error("DuckDuckGgo failed", e);
            failCounter.increment();
            return Collections.emptySet();
        }
        Elements results = pageWithSearchResults
                .getElementById(LINKS)
                .getElementsByClass(RESULTS_LINKS);

        Set<String> searchResult = results.stream()
                .map(this::extractUrl)
                .collect(Collectors.toSet());

        timer.stop();
        return searchResult;
    }

    private String extractUrl(Element node) {
        return node.getElementsByClass(LINKS_MAIN)
                .first()
                .getElementsByTag(A).first()
                .attr(HREF);
    }
}
