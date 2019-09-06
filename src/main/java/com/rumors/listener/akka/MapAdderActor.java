package com.rumors.listener.akka;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.rumors.wordcount.MapAdder;
import com.rumors.wordcount.TopCollector;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MapAdderActor extends AbstractActor {

    private final MapAdder adder;
    private final Map<String, Map<String, Integer>> totals = new HashMap<>();

    MapAdderActor(MapAdder adder, TopCollector topCollector) {
        this.adder = adder;
    }

    static Props props(MapAdder adder, TopCollector topCollector) {
        return Props.create(MapAdderActor.class, adder, topCollector);
    }

    public Receive createReceive() {
        return receiveBuilder()
                .match(Add.class, add -> {
                    Map<String, Integer> total = totals.get(add.topic);
                    if (total != null) {
                        adder.add(add.count, total);
                    } else {
                        totals.put(add.topic, add.count);
                    }
                })
                .match(Execute.class, command -> {
                    command.function.apply(totals.getOrDefault(command.topic, Collections.emptyMap()));
                }).build();
    }

    static class Execute {
        final Function<Map<String, Integer>, Void> function;
        final String topic;

        Execute(Function<Map<String, Integer>, Void> function, String topic) {
            this.function = function;
            this.topic = topic;
        }
    }

    static class Add {
        final Map<String, Integer> count;
        final String topic;


        Add(Map<String, Integer> count, String topic) {
            this.count = count;
            this.topic = topic;
        }
    }

}
