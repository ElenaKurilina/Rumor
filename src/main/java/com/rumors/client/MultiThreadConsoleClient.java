package com.rumors.client;

import com.rumors.listener.akka.AkkaRumorListener;
import com.rumors.web.DuckDuckGo;
import com.rumors.web.WebPageReader;
import com.rumors.wordcount.WordCounter;
import com.rumors.words.Exclude;

public class MultiThreadConsoleClient {

    private final static ConsoleActions actions = new ConsoleActions();

    public static void main(String[] args) {
        WordCounter counter = new WordCounter(Exclude.words, 3);
        AkkaRumorListener rumorListener = new AkkaRumorListener(new DuckDuckGo(), new WebPageReader(), counter, 20);
        rumorListener.start();
        System.out.println("Type \'exit\' to exit the app.");

        while (true) {
            String input = actions.askForTopic();

            actions.quitIfRequired(input, rumorListener::stop);

            rumorListener.listenToRumors(input);
        }
    }

}


