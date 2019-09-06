package com.rumors.listener.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.rumors.wordcount.WordCounter;

import java.util.Map;

public class WordCounterActor extends AbstractActor {

    private final WordCounter counter;
    private final ActorRef countReducer;

    WordCounterActor(WordCounter counter, ActorRef countReducer) {
        this.counter = counter;
        this.countReducer = countReducer;
    }

    static Props props(WordCounter counter, ActorRef countReducer) {
        return Props.create(WordCounterActor.class, counter, countReducer);
    }

    public Receive createReceive() {
        return receiveBuilder()
                .match(Count.class, article -> {
                    Map<String, Integer> count = counter.countWords(article.text, article.topic);
                    countReducer.tell(new MapAdderActor.Add(count, article.topic), ActorRef.noSender());
                })
                .build();
    }

    static class Count {
        final String text;
        final String topic;

        Count(String text, String topic) {
            this.text = text;
            this.topic = topic;
        }
    }

}
