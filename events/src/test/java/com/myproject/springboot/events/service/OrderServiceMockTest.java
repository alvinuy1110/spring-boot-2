package com.myproject.springboot.events.service;

import java.util.List;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.myproject.springboot.events.config.EventConfig;
import com.myproject.springboot.events.domain.OrderEvent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

//@SpringBootTest(
//        webEnvironment = SpringBootTest.WebEnvironment.NONE,
//        properties = {"spring.config.name:events", "spring.profiles.active:test-listener",
//                "spring.config.location:../config/",})
@Slf4j
public class OrderServiceMockTest{ 

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private OrderService orderService;

    @Captor
    protected ArgumentCaptor<Object> publishEventCaptor;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
    
    @AfterMethod
    public void teardown() {
        Mockito.reset();
    }

    @Test
    public void testOrderService_countTo3_publishesEvents() {

        // given
        OrderEvent orderEvent = createOrderEvent();
        orderEvent.setId(14);

        orderService.submitOrder(orderEvent);
        verifyPublishedEvents(orderEvent);
    }

    protected void verifyPublishedEvents(Object... events) {
        Mockito.verify(applicationEventPublisher, Mockito.times(events.length)).publishEvent(publishEventCaptor.capture());
        List<Object> capturedEvents = publishEventCaptor.getAllValues();

        for (int i = 0; i < capturedEvents.size(); i++) {
            assertThat(capturedEvents.get(i)).isInstanceOf((events[i].getClass()));
            log.info("Event name: {}",events[i].getClass().getName());
            assertThat(capturedEvents.get(i)).isEqualTo(events[i]);
        }
    }
    

    private OrderEvent createOrderEvent() {
        OrderEvent orderEvent = new OrderEvent();

        return orderEvent;
    }

}
