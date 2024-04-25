package com.systemdesign.slidingwindowlog.controller;

import com.systemdesign.slidingwindowlog.dto.response.SlidingWindowLogProfileResponse;
import com.systemdesign.slidingwindowlog.dto.response.SlidingWindowLogResponse;
import com.systemdesign.slidingwindowlog.service.SlidingWindowLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("sliding-window-log")
public class SlidingWindowLogController {

    private final SlidingWindowLogService slidingWindowLogService;

    @GetMapping
    public Mono<ResponseEntity<Flux<SlidingWindowLogResponse>>> findAllSlidingWindowLog() {
        return Mono.just(
                ResponseEntity.status(OK)
                        .body(slidingWindowLogService.findAllSlidingWindowLog())
        );
    }

    @PostMapping
    public Mono<ResponseEntity<SlidingWindowLogProfileResponse>> createSlidingWindowLog() {
        return slidingWindowLogService.createSlidingWindowLog()
                .map(response -> ResponseEntity.status(CREATED).body(response));
    }
}
