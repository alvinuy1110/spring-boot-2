# Events example

This will demonstrate using Spring Events

## Features

* publish sync [OrderService](./src/main/java/com/myproject/springboot/events/service/OrderService.java)

* consume sync [OrderProcessor](./src/main/java/com/myproject/springboot/events/service/OrderProcessor.java)
* consume sync, conditional [OrderProcessor](./src/main/java/com/myproject/springboot/events/service/OrderProcessor.java)
* consume async [OrderProcessor](./src/main/java/com/myproject/springboot/events/service/OrderProcessor.java)

* publish unit test [OrderServiceMockTest](./src/test/java/com/myproject/springboot/events/service/OrderServiceMockTest.java)
* publish-consume sync IT (unit test) [OrderServiceTest](./src/test/java/com/myproject/springboot/events/service/OrderServiceTest.java)

## Publishing

### Synchronous

* Use ApplicationEventPublisher

### Asynchronous


## Consuming

* Use EventListener annotation
* Use ASync annotation
* Add EnableAsync in configuration

## Transactions

* Use TransactionalEventListener

```
AFTER_COMMIT (default) is used to fire the event if the transaction has completed successfully
AFTER_ROLLBACK – if the transaction has rolled back
AFTER_COMPLETION – if the transaction has completed (an alias for AFTER_COMMIT and AFTER_ROLLBACK)
BEFORE_COMMIT is used to fire the event right before transaction commit
```

see https://www.baeldung.com/spring-events

## TODO
