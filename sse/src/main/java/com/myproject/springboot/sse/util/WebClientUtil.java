package com.myproject.springboot.sse.util;

import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class WebClientUtil {


    public static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info(">>> Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers()
                         .forEach((name, values) -> values.forEach(value -> log.info(">>>> {}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }

    public static ExchangeFilterFunction logResponseStatus() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("<<< Response Status {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }
}
