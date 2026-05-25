package com.steganohub.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class MetricsService {

    private final Counter encodeCounter;
    private final Counter decodeCounter;
    private final Counter errorCounter;
    private final MeterRegistry registry;

    @Autowired
    public MetricsService(MeterRegistry registry) {
        this.registry = registry;
        this.encodeCounter = Counter.builder("stegano.encode.total")
                .description("Anzahl der Encode-Operationen")
                .register(registry);

        this.decodeCounter = Counter.builder("stegano.decode.total")
                .description("Anzahl der Decode-Operationen")
                .register(registry);

        this.errorCounter = Counter.builder("stegano.errors.total")
                .description("Anzahl der Fehler")
                .register(registry);
    }

    public void incrementEncode() {
        encodeCounter.increment();
    }

    public void incrementDecode() {
        decodeCounter.increment();
    }

    public void incrementError() {
        errorCounter.increment();
    }

    public Timer.Sample startTimer() {
        return Timer.start(registry);
    }

    public void stopTimer(Timer.Sample sample, String operation) {
        sample.stop(Timer.builder("stegano." + operation + ".duration")
                .description("Dauer der " + operation + "-Operation")
                .register(registry));
    }
}