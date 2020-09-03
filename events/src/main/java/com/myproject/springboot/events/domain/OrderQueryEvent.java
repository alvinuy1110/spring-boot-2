package com.myproject.springboot.events.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderQueryEvent implements Serializable {

    private long id;
    private Date date;
    private List<OrderItem> orderItems;
}
