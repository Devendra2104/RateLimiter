package com.devo_bhai.rateLim.interceptor;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

public class RateLimitInterceptor implements HandlerInterceptor {
    private static final String HEADER_LIMIT_REMAINING = "X-Rate-Limit-Remaining";
    private static final String HEADER_RETRY_AFTER = "X-Rate-Limit-Retry-After-Seconds";

    private final Bucket tokenBucket;
    private final int numTokens;

    public RateLimitInterceptor(Bucket bucket, int numTokens) {
        this.tokenBucket = bucket;
        this.numTokens = numTokens;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ConsumptionProbe probe = this.tokenBucket.tryConsumeAndReturnRemaining(this.numTokens);
        if (probe.isConsumed()) {
            response.addHeader(HEADER_LIMIT_REMAINING,
                    Long.toString(probe.getRemainingTokens()));
            return true;
        }

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value()); // 429
        response.addHeader(HEADER_RETRY_AFTER,
                Long.toString(TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill())));

        return false;
    }
}
