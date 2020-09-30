# SSE

Server Sent Events (SSE)

## Dependency

* JDK 8+

## Use Cases

### Server

#### MVC

Use SSEEmitter

#### Reactive

Use Mono / Flux

##### Error Handling

* https://blog.rpuch.com/2019/08/28/handling-sse-errors-in-spring-webflux.html

### Client

* more info https://www.callicoder.com/spring-5-reactive-webclient-webtestclient-examples/

#### Curl

```
curl http://localhost:8080/sse/stream-sse
or
curl http://localhost:8080/sse/stream-sse-mvc
```



## Testing

* https://howtodoinjava.com/spring-webflux/webfluxtest-with-webtestclient/
* https://docs.spring.io/spring/docs/current/spring-framework-reference/pdf/testing-webtestclient.pdf
* https://www.callicoder.com/spring-5-reactive-webclient-webtestclient-examples/
* https://www.baeldung.com/reactive-streams-step-verifier-test-publisher

## Reference

* https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.htm
* https://www.baeldung.com/spring-5-webclient
* https://www.baeldung.com/spring-webflux
