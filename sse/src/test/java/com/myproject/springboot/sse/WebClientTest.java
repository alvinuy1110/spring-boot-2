package com.myproject.springboot.sse;

import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.myproject.springboot.sse.controller.SSEController;
import com.myproject.springboot.sse.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/*
This is a unit test example for controllers.  Dont use MockMVC
 */
@Slf4j
//@WebFluxTest(controllers = SSEController.class)

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)

@WebFluxTest
@ContextConfiguration(classes = TestConfiguration.class)
public class WebClientTest { //extends AbstractTestNGSpringContextTests{

    private WebTestClient webClient;

    @MockBean
    private PersonService service;

    @BeforeMethod
    public void initClient() {
        webClient = WebTestClient.bindToController(new SSEController()).build();
    }

    private static final ParameterizedTypeReference<ServerSentEvent<String>> typeRef =
            new ParameterizedTypeReference<ServerSentEvent<String>>() {

            };

    @Test
    public void testSSE() {

        //        FluxExchangeResult<ServerSentEvent<String>> 
        Flux<ServerSentEvent<String>> result =
                webClient.get().uri("/stream-sse").accept(MediaType.TEXT_EVENT_STREAM).exchange().expectStatus()
                         .is2xxSuccessful().expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
                         .returnResult(typeRef).getResponseBody();

        //        result.log();
        //        log.info("Body: {}", result.getResponseBody());
        //        StepVerifier.create(result.getResponseBody())
        //                    .expectSubscription()
        ////                    .expectNoEvent(Duration.ofSeconds(5))
        //                    .thenCancel()
        //                    .verify();

        // verify the event/s

        Flux<ServerSentEvent<String>> eventFlux = result;
        StepVerifier.create(eventFlux)
                    // use this if you know whats coming next
                    //                    .expectNext(S)
                    //                    .expectNextCount(4)
                    // consume the first event
                    .consumeNextWith(p -> {
                        log.info("i:{}, event:{}, data:{}", p.id(), p.event(), p.data());
                    }).thenCancel().verify();
    }
}
