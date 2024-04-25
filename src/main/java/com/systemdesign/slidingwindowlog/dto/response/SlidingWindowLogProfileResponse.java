package com.systemdesign.slidingwindowlog.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SlidingWindowLogProfileResponse {

    private List<SlidingWindowLogResponse> counters;

    public static SlidingWindowLogProfileResponse from(List<SlidingWindowLogResponse> counters) {
        return SlidingWindowLogProfileResponse.builder()
                .counters(counters)
                .build();
    }
}
