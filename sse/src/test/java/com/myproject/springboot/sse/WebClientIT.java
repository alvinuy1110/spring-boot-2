package com.myproject.springboot.sse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClientConfigurer;
import org.testng.annotations.Test;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static com.myproject.springboot.sse.util.WebClientUtil.logRequest;
import static com.myproject.springboot.sse.util.WebClientUtil.logResponseStatus;

/**
 * Created by user on 14/09/20.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
//@AutoConfigureWebTestClient(timeout = "10000")//10 seconds
public class WebClientIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private WebTestClient webTestClient;

    @LocalServerPort
    private int randomServerPort;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private static final ParameterizedTypeReference<ServerSentEvent<String>> typeRef =
            new ParameterizedTypeReference<ServerSentEvent<String>>() {

            };

    @Test
    public void webclient_getStreamSSE_success() {

        String url = "http://localhost:" + randomServerPort + contextPath;

        //  WebTestClient webTestClient = createWebClient(url);
        WebTestClientConfigurer webTestClientConfigurer = (b, h, c) -> {
            b.baseUrl(url);
            b.filter(logRequest()).filter(logResponseStatus());
        };
        webTestClient = webTestClient.mutateWith(webTestClientConfigurer);

        FluxExchangeResult<ServerSentEvent<String>> result = webTestClient.get().uri("/stream-sse")

                                                                          .accept(MediaType.TEXT_EVENT_STREAM)
                                                                          .exchange().expectStatus().isOk()
                                                                          //                     .expectHeader()
                                                                          // .contentType(MediaType.TEXT_EVENT_STREAM)

                                                                          // Not for streaming
                                                                          //                     .expectBody()
                                                                          //                     .consumeWith
                                                                          // (response -> {
                                                                          //                             log.info
                                                                          // ("Body: {}", response.getResponseBody());
                                                                          //                             Assertions
                                                                          // .assertThat(response.getResponseBody())
                                                                          // .isNotNull();
                                                                          //                         
                                                                          //                     }).returnResult();

                                                                          // Must end with returnResult for Streams
                                                                          .returnResult(typeRef);

        Flux<ServerSentEvent<String>> eventFlux = result.getResponseBody();
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
