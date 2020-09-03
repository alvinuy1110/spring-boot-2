package com.myproject.springboot.events.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationEvent;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderCancelledEvent extends ApplicationEvent implements Serializable {

    private long id;
    private Date date;
    private List<OrderItem> orderItems;
    
    public OrderCancelledEvent(Object source) {
        super(source);
    }
}
