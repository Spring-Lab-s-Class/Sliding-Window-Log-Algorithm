package com.systemdesign.slidingwindowlog.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlidingWindowLogResponse {

    private String key;
    private Long requestCount;

    public static SlidingWindowLogResponse from(String key, Long requestCount) {
        return SlidingWindowLogResponse.builder()
                .key(key)
                .requestCount(requestCount)
                .build();
    }
}
