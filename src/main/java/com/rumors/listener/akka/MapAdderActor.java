package com.rumors.listener.akka;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.rumors.wordcount.MapAdder;
import com.rumors.wordcount.TopCollector;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapAdderActor extends AbstractActor {

    private final MapAdder adder;
    private final TopCollector topCollector;
    private final Map<String, Map<String, Integer>> totals = new HashMap<>();

    MapAdderActor(MapAdder adder, TopCollector topCollector) {
        this.adder = adder;
        this.topCollector = topCollector;
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
                .match(Print.class, total -> {
                    if (totals.containsKey(total.topic)) {
                        LinkedHashMap<String, Integer> top = TopCollector.collectOrderedTop(totals.get(total.topic), total.top);
                        System.out.println("Result for " + total.topic);
                        System.out.println(top.toString());
                    } else {
                        System.out.println("Listening...");
                    }
                }).build();
    }

    static class Print {
        final String topic;
        final int top;


        Print(String topic, int top) {
            this.topic = topic;
            this.top = top;
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
