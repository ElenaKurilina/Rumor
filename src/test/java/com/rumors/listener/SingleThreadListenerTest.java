package com.rumors.listener;

import com.rumors.web.DuckDuckGo;
import com.rumors.web.WebPageReader;
import com.rumors.wordcount.MapAdder;
import com.rumors.wordcount.WordCounter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;

class SingleThreadListenerTest {

    private Listener listener;

    @BeforeEach
    void setup() {
        WordCounter counter = new WordCounter(Collections.emptySet(), 1);
        listener = new SingleThreadListener(new DuckDuckGo(), new WebPageReader(), counter, new MapAdder());
        listener.start();
    }

    @AfterEach
    void cleanup() {
        listener.stop();
    }

    @Test
    void listenToRumors() {
        String topic = "coffee";

        listener.listenToRumors(topic, (totals) -> {
            assertFalse(totals.isEmpty());
            return null;
        });


    }
}