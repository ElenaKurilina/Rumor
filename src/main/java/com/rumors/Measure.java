package com.rumors;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.LongTaskTimer;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.statsd.StatsdConfig;
import io.micrometer.statsd.StatsdFlavor;
import io.micrometer.statsd.StatsdMeterRegistry;

import java.time.Duration;

public class Measure {
    private static final Duration reportStep = Duration.ofSeconds(1);

    private static final StatsdConfig config = new StatsdConfig() {
        @Override
        public String get(String k) {
            return null;
        }

        @Override
        public StatsdFlavor flavor() {
            return StatsdFlavor.DATADOG;
        }

        @Override
        public Duration step() {
            return reportStep;
        }
    };

    public static final MeterRegistry meterRegistry = new StatsdMeterRegistry(config, Clock.SYSTEM);

    public static LongTaskTimer.Sample startTimer(String name) {
        return LongTaskTimer.builder(name).register(Measure.meterRegistry).start();
    }


}
