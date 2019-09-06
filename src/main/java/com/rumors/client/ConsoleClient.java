package com.rumors.client;

import com.rumors.listener.Listener;
import com.rumors.listener.akka.AkkaRumorListener;
import com.rumors.web.DuckDuckGo;
import com.rumors.web.WebPageReader;
import com.rumors.wordcount.MapAdder;
import com.rumors.wordcount.TopCollector;
import com.rumors.wordcount.WordCounter;
import com.rumors.words.Exclude;

public class ConsoleClient {

    private static ConsoleActions actions = new ConsoleActions();

    public static void main(String[] args) {
        WordCounter counter = new WordCounter(Exclude.words, 3);
//        Listener rumorListener = new SingleThreadListener(new DuckDuckGo(), new WebPageReader(), counter, new MapAdder());
        Listener rumorListener = new AkkaRumorListener(new DuckDuckGo(), new WebPageReader(), counter, new MapAdder());

        actions.printIntro();

        rumorListener.start();
        while (true) {
            String input = actions.askForTopic();
            actions.quitIfRequired(input, rumorListener::stop);

            rumorListener.listenToRumors(input, (totals) -> {
                System.out.println("========================");
                System.out.println(input + ":");
                TopCollector.collectOrderedTop(totals, 5)
                        .forEach((word, count) -> System.out.println(word + " " + count));
                System.out.println("========================");
                return null;
            });
        }
    }

}


