package com.rumors.search;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class DuckDuckGo implements SearchEngine {

    private final static String DUCKDUCKGO_SEARCH_URL = "https://duckduckgo.com/html/?q=";

    @Override
    public Set<String> getLinksFor(String query) {
        Document pageWithSearchResults;
        try {
            pageWithSearchResults = Jsoup.connect(DUCKDUCKGO_SEARCH_URL + query).get();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptySet();
        }
        Elements results = pageWithSearchResults
                .getElementById("links")
                .getElementsByClass("results_links");
        return results.stream()
                .map(this::extractUrl)
                .collect(Collectors.toSet());
    }

    private String extractUrl(Element node) {
        return node.getElementsByClass("links_main")
                .first()
                .getElementsByTag("a").first()
                .attr("href");
    }
}
