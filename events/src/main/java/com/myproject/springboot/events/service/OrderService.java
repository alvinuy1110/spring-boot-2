package com.myproject.springboot.events.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.stereotype.Component;

import com.myproject.springboot.events.domain.OrderCancelledEvent;
import com.myproject.springboot.events.domain.OrderEvent;
import com.myproject.springboot.events.domain.OrderQueryEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class OrderService {

    private final ApplicationEventPublisher publisher;
    private final ApplicationEventMulticaster eventMulticaster;
    
    public void submitOrder(OrderEvent orderEvent) {
        
        log.info("Submitting order...");
        
        publisher.publishEvent(orderEvent);
        log.info("Submitted order");
    }

    public void submitOrderQuery(OrderQueryEvent orderQueryEvent) {

        log.info("Submitting order query...");

        publisher.publishEvent(orderQueryEvent);
        log.info("Submitted order query");
    }
    public void submitOrderCancellation(OrderCancelledEvent orderCancelledEvent) {

        log.info("Submitting order cancellation...");

        eventMulticaster.multicastEvent(orderCancelledEvent);
        log.info("Submitted order cancellation");
    }

    private OrderEvent createOrderEvent() {
        OrderEvent orderEvent = new OrderEvent();
        
        return orderEvent;
    }
}
