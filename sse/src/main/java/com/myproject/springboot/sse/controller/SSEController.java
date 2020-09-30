package com.myproject.springboot.sse.controller;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.myproject.springboot.sse.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * This is just a simple controller example
 */
//@Controller
// need to use this or else the unit testing will be missing some harness
@RestController
@Slf4j
public class SSEController {

    // when using a service
    // see https://blog.rpuch.com/2019/08/29/unit-testing-spring-webflux-streaming-controller.html
    private PersonService personService;
    
    // Using reactive
    @GetMapping(value = "/stream-sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamEvents() {
        return Flux.interval(Duration.ofSeconds(1))
                   .map(sequence -> ServerSentEvent.<String>builder().id(String.valueOf(sequence))
                                                                     .event("periodic-event")
                                                                     .data("SSE - " + LocalTime.now().toString())

                                                                     // this is to prevent say a proxy from killing 
                                                                     // the connnection
                                                                     .comment("keep alive")
                                                                     .build());
    }

    // Using MVC way
    @GetMapping(value = "/stream-sse-mvc", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamSseMvc() {
        SseEmitter emitter = new SseEmitter();
        ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
        sseMvcExecutor.execute(() -> {
            try {
                for (int i = 0; true; i++) {
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                                                                 .data("SSE MVC - " + LocalTime.now().toString())
                                                                 .id(String.valueOf(i))
                                                                 .name("sse event - mvc");
                    emitter.send(event);
                    Thread.sleep(1000);
                }
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }
}
