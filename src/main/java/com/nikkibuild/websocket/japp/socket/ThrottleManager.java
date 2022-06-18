package com.nikkibuild.websocket.japp.socket;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;

@Singleton
class ThrottleManager {
    private final Bandwidth limit  = Bandwidth.simple(5, Duration.ofMinutes(1));
    private final Bucket    bucket = Bucket.builder().addLimit(limit).build();

    @Inject
    ThrottleManager() {
    }

    boolean canSend() {
        return bucket.tryConsume(1);
    }
}