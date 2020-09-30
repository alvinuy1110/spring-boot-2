package com.myproject.springboot.sse.client;

import java.time.LocalTime;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.myproject.springboot.sse.exception.SSEException;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

/**
 *Sample client
 */
@Slf4j
public class SSEClient {

    public static void main(String[] args) throws Exception {
        SSEClient sseClient = new SSEClient();

        String url = "http://localhost:8080/sse";
        String uri = "/stream-sse-mvc";
        sseClient.consumeServerSentEvent(url, uri);

        // Sleep to see some stuff
        Thread.sleep(10000);
    }

    public void consumeServerSentEvent(String url, String uri) {
        // customized client
        WebClient client = createWebClient(url);

        // simple web client
        //        WebClient client = WebClient.create(url);
        ParameterizedTypeReference<ServerSentEvent<String>> type =
                new ParameterizedTypeReference<ServerSentEvent<String>>() {

                };

        //@formatter:off
        // Customize GET/ POST and other stuff like body, headers, etc.
        Flux<ServerSentEvent<String>> eventStream = client.get()

                                                          .uri(uri)
                                                          .retrieve()
                                                          
                                                          // Handling errors
                                                          .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                                                                Mono.error(new SSEException())
                                                          )
                                                          .onStatus(HttpStatus::is5xxServerError, clientResponse ->
                                                                Mono.error(new SSEException())
                                                            )
                                                          .bodyToFlux(type);

        //@formatter:on
        eventStream.subscribe(content -> log
                        .info("Time: {} - event: name[{}], id [{}], content[{}] ", LocalTime.now(), content.event(),
                                content.id(), content.data()), error -> log.error("Error receiving SSE: {}", error),
                () -> log.info("Completed!!!"));
    }

    private WebClient createWebClient(String baseUrl) {

        // configure timeouts
        TcpClient tcpClient =
                TcpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000).doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                });

        WebClient client = WebClient.builder()
                                    // inject the tcp client
                                    .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                                    .baseUrl(baseUrl).defaultCookie("cookieKey", "cookieValue")
                                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                    .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080"))
                                    
                                    // add filter/s
                                    // basic auth example
//                                    .filter(ExchangeFilterFunctions
//                                            .basicAuthentication(username, token))
                                    .filter(logRequest())
                                    .filter(logResponseStatus())
                                    .build();

        return client;
    }


    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info(">>> Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers()
                         .forEach((name, values) -> values.forEach(value -> log.info(">>>> {}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponseStatus() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("<<< Response Status {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }
}
