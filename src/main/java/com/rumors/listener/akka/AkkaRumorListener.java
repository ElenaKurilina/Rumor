package com.rumors.listener.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.routing.RoundRobinPool;
import com.rumors.listener.Listener;
import com.rumors.web.SearchEngine;
import com.rumors.web.WebPageReader;
import com.rumors.wordcount.MapAdder;
import com.rumors.wordcount.TopCollector;
import com.rumors.wordcount.WordCounter;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.IntStream;

public class AkkaRumorListener implements Listener {

    private final SearchEngine engine;
    private final WebPageReader pageReader;
    private final WordCounter counter;

    private ActorRef readingActor;
    private ActorRef countReducer;
    private ActorSystem system;

    private final ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(1);
    private MapAdder adder;

    public AkkaRumorListener(SearchEngine engine, WebPageReader pageReader, WordCounter counter, MapAdder adder) {
        this.engine = engine;
        this.pageReader = pageReader;
        this.counter = counter;
        this.adder = adder;
    }

    @Override
    public void start() {
        system = ActorSystem.create("rumor-actor-system");

        countReducer = system.actorOf(MapAdderActor.props(adder, new TopCollector()), "count-reducer-actor");
        ActorRef counterActor = system.actorOf(WordCounterActor.props(counter, countReducer), "counter-actor");
        readingActor = system.actorOf(ArticleReaderActor.props(pageReader, counterActor)
                .withRouter(new RoundRobinPool(5)), "reader-actor");
    }

    @Override
    public void listenToRumors(String topic, Function<Map<String, Integer>, Void> callback) {
        Set<String> linksFor = engine.findLinksFor(topic);
        for (String l : linksFor) {
            readingActor.tell(new ArticleReaderActor.Read(l, topic), ActorRef.noSender());
        }

        IntStream.range(0, 5).forEach(i -> {
            scheduledExecutor.schedule(() -> countReducer
                    .tell(new MapAdderActor.Execute(callback, topic), ActorRef.noSender()), i, TimeUnit.SECONDS);
        });
    }

    @Override
    public void stop() {
        system.terminate();
    }

}
