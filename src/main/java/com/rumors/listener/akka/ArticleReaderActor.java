package com.rumors.listener.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.rumors.web.WebPageReader;

public class ArticleReaderActor extends AbstractActor {

    private final WebPageReader pageReader;
    private final ActorRef counter;

    ArticleReaderActor(WebPageReader pageReader, ActorRef next) {
        this.pageReader = pageReader;
        this.counter = next;
    }

    static Props props(WebPageReader webPageReader, ActorRef counter) {
        return Props.create(ArticleReaderActor.class, webPageReader, counter);
    }

    public Receive createReceive() {
        return receiveBuilder()
                .match(Read.class, read -> {
                    String article = pageReader.read(read.url);
                    counter.tell(new WordCounterActor.Article(article, read.topic), getSender());
                })
                .build();
    }

    static class Read {
        final String url;
        final String topic;

        Read(String link, String topic) {
            this.url = link;
            this.topic = topic;
        }
    }

}
