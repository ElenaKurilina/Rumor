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

    public static void main(String[] args) {
        WordCounter counter = new WordCounter(Exclude.words, 3);
        RumorListener rumorListener = new RumorListener(new DuckDuckGo(), new WebPageReader(), counter, 20);

        String topic = askForTopic();

        LinkedHashMap<String, Integer> rumors = rumorListener.listenToRumors(topic);
        rumors.forEach((word, count) -> System.out.println(count + " " + word));
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
}


