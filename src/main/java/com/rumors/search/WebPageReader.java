package com.rumors.search;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class WebPageReader {

    public String read(String url) {
        Document article;
        try {
            article = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        return article.body().text();
    }
}
