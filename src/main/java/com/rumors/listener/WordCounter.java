package com.rumors.listener;

import com.rumors.Measure;
import io.micrometer.core.instrument.LongTaskTimer;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WordCounter {

    private static final String SPACE = " ";
    private static final Pattern notLetter = Pattern.compile("[^a-zA-Z]");
    private final Set<String> exclude;
    private final int minWordLength;

    public WordCounter(Set<String> exclude, int minWordLength) {
        this.exclude = exclude;
        this.minWordLength = minWordLength;
    }

    Map<String, Integer> countWords(String text, String customExclude) {
        LongTaskTimer.Sample timer = Measure.startTimer("countArticle");
        Set<String> cleaned = Arrays.stream(customExclude.split(SPACE))
                .map(this::cleanWord)
                .collect(Collectors.toSet());

        Map<String, Integer> count = new HashMap<>();
        Arrays.stream(text.split(SPACE))
                .map(this::cleanWord)
                .filter(word -> shouldBeCounted(word, cleaned))
                .forEach(word -> count.merge(word, 1, Integer::sum));
        timer.stop();
        return count;
    }

    private boolean shouldBeCounted(String word, Set<String> customExclude) {
        return word.length() > minWordLength
                && !exclude.contains(word)
                && !customExclude.contains(word);
    }

    private String cleanWord(String word) {
        return notLetter.matcher(word)
                .replaceAll("")
                .toLowerCase();
    }
}
