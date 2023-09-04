package com.devo_bhai.rateLim.config;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@EnableScheduling
public class RateLimiterConfig {

    private static final int RATE_LIMIT = 10; // 10 requests per second
    private static final int WINDOW_SIZE_SECONDS = 1;
    private static final int MAX_REQUESTS_PER_WINDOW = RATE_LIMIT * WINDOW_SIZE_SECONDS;
    private final AtomicInteger requestCount = new AtomicInteger(0);

    @Bean
    public AtomicInteger requestCount() {
        return requestCount;
    }

    @Scheduled(fixedRate = 1000) // Run every second
    public void resetRequestCount() {
        requestCount.set(0);
    }

    @Bean
    public RateLimiter rateLimiter(AtomicInteger requestCount) {
        return RateLimiter.create(MAX_REQUESTS_PER_WINDOW);
    }
}
