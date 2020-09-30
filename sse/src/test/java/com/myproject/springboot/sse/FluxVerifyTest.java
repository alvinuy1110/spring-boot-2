package com.myproject.springboot.sse;

import org.testng.annotations.Test;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@Slf4j
public class FluxVerifyTest {

    @Test
    public void testFluxStepVerifier() {
      
        // given
        Flux<String> source = Flux.just("John", "Monica", "Mark", "Cloe", "Frank", "Casper", "Olivia", "Emily", "Cate")
                                  .filter(name -> name.length() == 4)
                                  .map(String::toUpperCase);
        
        // then
        StepVerifier
                .create(source)
                .expectNext("JOHN")
                .expectNextMatches(name -> name.startsWith("MA"))
                .expectNext("CLOE", "CATE")
                .expectComplete()
                .verify();
    }
}
