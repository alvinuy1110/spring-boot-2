package com.myproject.springboot.events.domain;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * Created by user on 02/09/20.
 */
@Data
@ToString
public class OrderItem implements Serializable {

    private long id;
    private int quantity;
}
