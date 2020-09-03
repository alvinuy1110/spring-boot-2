package com.myproject.springboot.events.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.ToString;

/**
 * Created by user on 02/09/20.
 */
@Data
@ToString
public class OrderEvent implements Serializable {

    private long id;
    private Date date;
    private List<OrderItem> orderItems;
}
