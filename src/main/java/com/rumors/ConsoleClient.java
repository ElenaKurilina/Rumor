package com.rumors;

import com.rumors.listener.RumorListener;
import com.rumors.listener.WordCounter;
import com.rumors.search.DuckDuckGo;
import com.rumors.search.WebPageReader;
import com.rumors.words.Exclude;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;

public class ConsoleClient {

    private static final String EXIT = "exit";

    public static void main(String[] args) {
        WordCounter counter = new WordCounter(Exclude.words, 3);
        RumorListener rumorListener = new RumorListener(new DuckDuckGo(), new WebPageReader(), counter, 20);
        System.out.println("Type \'exit\' to exit the app.");

        while (true) {
            String input = askForTopic();
            quitIfRequired(input);

            LinkedHashMap<String, Integer> rumors = rumorListener.listenToRumors(input);
            rumors.forEach((word, count) -> System.out.println(count + " " + word));
        }
    }

    private static String askForTopic() {
        System.out.println("Give me a topic and we will tell you what people are saying about it.\n");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            return reader.readLine();
        } catch (IOException e) {
            return "";
        }
    }

    private static void quitIfRequired(String input) {
        String command = input.toLowerCase();
        if (EXIT.equals(command)) {
            System.exit(0);
        }
    }
}


