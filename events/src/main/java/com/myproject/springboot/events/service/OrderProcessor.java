package com.myproject.springboot.events.service;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.myproject.springboot.events.domain.OrderCancelledEvent;
import com.myproject.springboot.events.domain.OrderEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderProcessor {

    // To handle the event no conditions
    @EventListener
    public void handleOrder(OrderEvent orderEvent) {
        log.info("Handling order ");
        log.info("order event: {}", orderEvent);
    }

    // To handle the event with conditions
    @EventListener(condition = "#orderEvent.id >= 10 and #orderEvent.id <= 20")
    public void handleOrderBetween10And20(OrderEvent orderEvent) {
        log.info("Handling order between 10 and 20 ");
        log.info("order event: {}", orderEvent);
    }

    @Async
    @EventListener
    public void handleOrderAsync(OrderEvent orderEvent) {
        log.info("Handling order async");
        log.info("order event: {}", orderEvent);
    }


//    @Async
    @EventListener
    public void handleOrderCancel(OrderCancelledEvent orderCancelledEvent) {
        log.info("Handling order cancel");
        log.info("order event: {}", orderCancelledEvent);
    }
}
