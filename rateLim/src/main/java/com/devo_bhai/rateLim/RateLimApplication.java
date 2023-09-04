package com.devo_bhai.rateLim;

import com.devo_bhai.rateLim.interceptor.RateLimitInterceptor;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.grid.hazelcast.HazelcastProxyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@SpringBootApplication
public class RateLimApplication implements WebMvcConfigurer {

	@Autowired
	private HazelcastInstance hazelcastInstance;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		Refill refill = Refill.intervally(10, Duration.ofMinutes(1));
		Bandwidth limit = Bandwidth.classic(10, refill);
//		Bucket bucket = Bucket.builder().addLimit(limit).build();
		BucketConfiguration configuration = BucketConfiguration.builder().addLimit(limit)
				.build();
		IMap<String, byte[]> map = this.hazelcastInstance.getMap("bucket-map");
		HazelcastProxyManager<String> proxyManager = new HazelcastProxyManager<>(map);
		Bucket hazelcastBucket = proxyManager.builder().build("rate-limit", configuration);
		registry.addInterceptor(new RateLimitInterceptor(hazelcastBucket, 1))
				.addPathPatterns("/api/v1/area/**");
//		registry.addInterceptor(new RateLimitInterceptor(bucket, 1))
//				.addPathPatterns("/api/v1/area/**");
	}

	public static void main(String[] args) {
		SpringApplication.run(RateLimApplication.class, args);
	}

}
