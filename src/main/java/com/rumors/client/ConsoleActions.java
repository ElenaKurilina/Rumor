package com.rumors.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class ConsoleActions {

    private static final String EXIT = "exit";

    void printIntro() {
        System.out.println("Type \'exit\' to exit the app.");
    }

    String askForTopic() {
        System.out.println("Give me a topic and we will tell you what people are saying about it.\n");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            return reader.readLine();
        } catch (IOException e) {
            return "";
        }
    }

    void quitIfRequired(String input) {
        quitIfRequired(input, null);
    }

    void quitIfRequired(String input, Runnable beforeQuit) {
        String command = input.toLowerCase();
        if (EXIT.equals(command)) {
            if (beforeQuit != null) {
                beforeQuit.run();
            }
            System.exit(0);
        }
    }
}


