package com.rumors.listener;

import java.util.Map;
import java.util.function.Function;

public interface Listener {

    void start();

    void stop();

    void listenToRumors(String topic, Function<Map<String, Integer>, Void> callback);
}
