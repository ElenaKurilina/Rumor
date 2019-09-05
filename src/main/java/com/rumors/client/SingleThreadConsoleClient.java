package com.rumors.client;

import com.rumors.listener.RumorListener;
import com.rumors.web.DuckDuckGo;
import com.rumors.web.WebPageReader;
import com.rumors.wordcount.MapAdder;
import com.rumors.wordcount.WordCounter;
import com.rumors.words.Exclude;

import java.util.LinkedHashMap;

public class SingleThreadConsoleClient {

    private static ConsoleActions actions = new ConsoleActions();

    public static void main(String[] args) {
        WordCounter counter = new WordCounter(Exclude.words, 3);
        RumorListener rumorListener = new RumorListener(new DuckDuckGo(), new WebPageReader(), counter, new MapAdder(), 20);

        actions.printIntro();

        while (true) {
            String input = actions.askForTopic();
            actions.quitIfRequired(input);

            LinkedHashMap<String, Integer> rumors = rumorListener.listenToRumors(input);
            rumors.forEach((word, count) -> System.out.println(count + " " + word));
        }
    }

}


