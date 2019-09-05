package com.rumors.listener.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.routing.RoundRobinPool;
import com.rumors.web.SearchEngine;
import com.rumors.web.WebPageReader;
import com.rumors.wordcount.MapAdder;
import com.rumors.wordcount.TopCollector;
import com.rumors.wordcount.WordCounter;

import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class AkkaRumorListener {

    private final SearchEngine engine;
    private final WebPageReader pageReader;
    private final WordCounter counter;
    private final int top;

    private ActorRef readingActor;
    private ActorRef countReducer;
    private ActorSystem system;

    private final ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(1);

    public AkkaRumorListener(SearchEngine engine, WebPageReader pageReader, WordCounter counter, int top) {
        this.engine = engine;
        this.pageReader = pageReader;
        this.counter = counter;
        this.top = top;
    }

    public void start() {
        system = ActorSystem.create("rumor-actor-system");

        countReducer = system.actorOf(MapAdderActor.props(new MapAdder(), new TopCollector()), "count-reducer-actor");
        ActorRef counterActor = system.actorOf(WordCounterActor.props(counter, countReducer), "counter-actor");
        readingActor = system.actorOf(ArticleReaderActor.props(pageReader, counterActor)
                .withRouter(new RoundRobinPool(5)), "reader-actor");
    }

    public void listenToRumors(String topic) {
        Set<String> linksFor = engine.getLinksFor(topic);
        for (String l : linksFor) {
            readingActor.tell(new ArticleReaderActor.Read(l, topic), ActorRef.noSender());
        }

        IntStream.range(0, 10).forEach(i -> {
            scheduledExecutor.schedule(() -> countReducer
                    .tell(new MapAdderActor.Print(topic, top), ActorRef.noSender()), i, TimeUnit.SECONDS);
        });
    }

    public void stop() {
        system.terminate();
    }

}
