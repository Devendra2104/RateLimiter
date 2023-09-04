package com.devo_bhai.rateLim.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitService {

    private static final int REQUESTS_PER_MINUTE = 10;
    private final Bucket sharedBucket;

    public RateLimitService() {
        // Define the rate limit: 10 requests per minute
        Bandwidth limit = Bandwidth.classic(REQUESTS_PER_MINUTE, Refill.intervally(REQUESTS_PER_MINUTE, Duration.ofMinutes(1)));

        // Create a shared bucket
        this.sharedBucket = Bucket4j.builder().addLimit(limit).build();
    }

    public boolean allowRequest() {
        // Synchronize access to the shared bucket
        synchronized (sharedBucket) {
            // Consume a token from the shared bucket for this request
            return sharedBucket.tryConsume(1);
        }
    }
}

