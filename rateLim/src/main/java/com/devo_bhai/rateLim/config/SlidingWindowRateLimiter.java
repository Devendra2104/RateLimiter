package com.devo_bhai.rateLim.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Getter
@Setter
public class SlidingWindowRateLimiter {

    private static final int RATE_LIMIT = 10; // 10 requests per second
    private final int WINDOW_SIZE_SECONDS = 1;
    private final int MAX_REQUESTS_PER_WINDOW = RATE_LIMIT * WINDOW_SIZE_SECONDS;;

    private final LinkedBlockingDeque<Long> requestTimestamps = new LinkedBlockingDeque<>(MAX_REQUESTS_PER_WINDOW);
    private final AtomicInteger requestCount = new AtomicInteger(0);


    public boolean tryAcquire() throws Exception {
        long currentTimestamp = System.currentTimeMillis();
        long windowStartTimestamp = currentTimestamp - TimeUnit.SECONDS.toMillis(WINDOW_SIZE_SECONDS);
        // Remove old timestamps from the queue
        while (!requestTimestamps.isEmpty() && requestCount.get() > 0 && requestTimestamps.peekFirst() < windowStartTimestamp) {
            requestTimestamps.pollFirst();
            requestCount.decrementAndGet();
        }

        if (requestCount.get() < MAX_REQUESTS_PER_WINDOW) {
            boolean isPushed = requestTimestamps.offerLast(currentTimestamp);
            if(!isPushed) {
                throw new Exception();
            }
            requestCount.incrementAndGet();
            return true;
        }
        return false;
    }
}
