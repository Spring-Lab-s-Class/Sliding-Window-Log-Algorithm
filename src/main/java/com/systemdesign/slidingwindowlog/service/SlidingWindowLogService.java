package com.systemdesign.slidingwindowlog.service;

import com.systemdesign.slidingwindowlog.dto.response.SlidingWindowLogProfileResponse;
import com.systemdesign.slidingwindowlog.dto.response.SlidingWindowLogResponse;
import com.systemdesign.slidingwindowlog.exception.RateExceptionCode;
import com.systemdesign.slidingwindowlog.exception.RateLimitExceededException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlidingWindowLogService {

    private final ReactiveRedisTemplate<String, Object> redisTemplate;
    private final static String SLIDING_WINDOW_KEY = "SlidingWindow:"; // 키
    private final static long SLIDING_WINDOW_MAX_REQUEST = 1000; // 최대 요청 허용 수
    private final static long SLIDING_WINDOW_DURATION = 60; // 60초

    public Mono<SlidingWindowLogProfileResponse> createSlidingWindowLog() {
        long currentTimestamp = System.currentTimeMillis();
        String redisKey = generateRedisKey("requests");
        log.info("Sliding Window log created. key: {}", redisKey);

        return redisTemplate.opsForZSet()
                .removeRangeByScore(redisKey, Range.closed(0D, calculateTimeRange()))
                .flatMap(removed -> redisTemplate.opsForZSet().count(redisKey, Range.closed(calculateTimeRange(), (double) currentTimestamp)))
                .flatMap(count -> {
                    if (count >= SLIDING_WINDOW_MAX_REQUEST) {
                        log.error("Rate limit exceeded. key: {}", redisKey);
                        return Mono.error(new RateLimitExceededException(RateExceptionCode.COMMON_TOO_MANY_REQUESTS, count));
                    }
                    return redisTemplate.opsForZSet()
                            .add(redisKey, String.valueOf(currentTimestamp), currentTimestamp)
                            .flatMap(added -> Mono.just(SlidingWindowLogProfileResponse.from(List.of(SlidingWindowLogResponse.from(redisKey, count + 1)))));
                });
    }

    public Flux<SlidingWindowLogResponse> findAllSlidingWindowLog() {
        String redisKey = generateRedisKey("requests");
        long currentTimestamp = System.currentTimeMillis();
        log.info("Sliding Window log find all. key: {}", redisKey);

        return redisTemplate.opsForZSet().rangeByScore(redisKey,
                        Range.closed(calculateTimeRange(), (double) currentTimestamp))
                .map(value -> {
                    log.info("Sliding Window log value: {}", value);
                    return SlidingWindowLogResponse.from(redisKey, Long.parseLong((String) value));
                });
    }

    private double calculateTimeRange() {
        long currentTimestamp = System.currentTimeMillis();
        return currentTimestamp - SLIDING_WINDOW_DURATION * 1000;
    }

    private String generateRedisKey(String requestType) {
        return SLIDING_WINDOW_KEY + requestType;
    }
}
