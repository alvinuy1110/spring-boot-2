package com.myproject.springboot.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.myproject.springboot.events.config.EventConfig;
import com.myproject.springboot.events.domain.OrderCancelledEvent;
import com.myproject.springboot.events.domain.OrderEvent;
import com.myproject.springboot.events.domain.OrderQueryEvent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This is an example to test publisher and consumer integration.  Uses a custom spring profile to activate different
 * setting.  The Listener is inject to retain info as to whether it was called or not.
 */
@SpringBootTest(classes = {OrderServiceTest.TestConfig.class, EventConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {"spring.config.name:events", "spring.profiles.active:test-listener",
                "spring.config.location:../config/",})
@Slf4j
public class OrderServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TestConfig.TestOrderProcessor testOrderProcessor;

    @BeforeMethod
    public void setup() {
        testOrderProcessor.reset();
    }
    @Test
    public void orderEvent_submitOrder_handleOrders() {
        // given
        assertThat(testOrderProcessor.isHandledOrder()).isFalse();
        assertThat(testOrderProcessor.isHandledOrderBetween10And20()).isFalse();
        OrderEvent orderEvent = createOrderEvent();
        orderEvent.setId(14);
        
        // when
        orderService.submitOrder(orderEvent);
        
        // then check events
        assertThat(testOrderProcessor.isHandledOrder()).isTrue();
        assertThat(testOrderProcessor.isHandledOrderBetween10And20()).isTrue();
    }


    @Test
    public void orderEventWithNoCondition_submitOrder_handleOrderConditionalNotCalled() {
        // given
        assertThat(testOrderProcessor.isHandledOrder()).isFalse();
        assertThat(testOrderProcessor.isHandledOrderBetween10And20()).isFalse();
        OrderEvent orderEvent = createOrderEvent();
        orderEvent.setId(9);

        // when
        orderService.submitOrder(orderEvent);

        // then check events
        assertThat(testOrderProcessor.isHandledOrder()).isTrue();
        assertThat(testOrderProcessor.isHandledOrderBetween10And20()).isFalse();
    }


    @Test
    public void orderQueryEvent_submitOrder_noOrderEventCalled() {
        // given
        assertThat(testOrderProcessor.isHandledOrder()).isFalse();
        assertThat(testOrderProcessor.isHandledOrderBetween10And20()).isFalse();
        OrderQueryEvent orderQueryEvent = createOrderQueryEvent();
        orderQueryEvent.setId(14);

        // when
        orderService.submitOrderQuery(orderQueryEvent);

        // then check events
        assertThat(testOrderProcessor.isHandledOrder()).isFalse();
        assertThat(testOrderProcessor.isHandledOrderBetween10And20()).isFalse();
    }



    @Test
    public void orderCancelEvent_submitOrder_noOrderEventCalled() {
        // given
        assertThat(testOrderProcessor.isHandledOrder()).isFalse();
        assertThat(testOrderProcessor.isHandledOrderBetween10And20()).isFalse();
        OrderCancelledEvent orderCancelledEvent = createOrderCancelledEvent();
        orderCancelledEvent.setId(14);

        // when
        orderService.submitOrderCancellation(orderCancelledEvent);

        // then check events
        assertThat(testOrderProcessor.isHandledOrder()).isFalse();
        assertThat(testOrderProcessor.isHandledOrderBetween10And20()).isFalse();
    }

    private OrderEvent createOrderEvent() {
        OrderEvent orderEvent = new OrderEvent();

        return orderEvent;
    }


    private OrderQueryEvent createOrderQueryEvent() {
        OrderQueryEvent orderQueryEvent = new OrderQueryEvent();

        return orderQueryEvent;
    }


    private OrderCancelledEvent createOrderCancelledEvent() {
        OrderCancelledEvent orderCancelledEvent = new OrderCancelledEvent("aaa");

        return orderCancelledEvent;
    }
    
    @Configuration
    @Profile("test-listener")
    public static class TestConfig {

        @Bean
        public OrderProcessor orderProcessor() {
            return new TestOrderProcessor();
        }

        @Data
        public class TestOrderProcessor extends OrderProcessor {

            private boolean handledOrder;
            private boolean handledOrderBetween10And20;

            @Override
            public void handleOrder(OrderEvent orderEvent) {
                log.info("test listener");
                super.handleOrder(orderEvent);
                handledOrder = true;

                log.info("test listener...end");
            }

            @Override
            public void handleOrderBetween10And20(OrderEvent orderEvent) {
                super.handleOrderBetween10And20(orderEvent);
                handledOrderBetween10And20 = true;
            }

            public void reset() {
                this.handledOrder=false;
                this.handledOrderBetween10And20=false;
            }
        }
        
    }
}
