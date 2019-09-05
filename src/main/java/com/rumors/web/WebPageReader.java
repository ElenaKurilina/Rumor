package com.rumors.web;

import com.rumors.Measure;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.LongTaskTimer;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class WebPageReader {

    private final static Logger logger = Logger.getLogger(WebPageReader.class);

    public String read(String url) {
        LongTaskTimer.Sample timer = Measure.startTimer("pageRead");

        Counter pageReadFail = Measure.meterRegistry.counter("pageReadFail");
        Document article;
        try {
            article = Jsoup.connect(url).get();
        } catch (IOException e) {
            logger.warn("Couldn't read page: " + url + e.getMessage());
            pageReadFail.increment();
            return "";
        }

        timer.stop();
        return article.body().text();
    }
}
