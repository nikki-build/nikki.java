package com.nikkibuild.websocket.japp.socket;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

import java.time.Duration;

public class ThrottleManager {
    private final Bucket bucket;

    public ThrottleManager(long capacity, long duration) {
        var limit = Bandwidth.simple(capacity, Duration.ofMinutes(duration));
        bucket = Bucket.builder().addLimit(limit).build();
    }

    boolean canSend() {
        return bucket.tryConsume(1);
    }
}