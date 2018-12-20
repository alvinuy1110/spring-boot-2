package com.myproject.springboot.rabbit.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
This represents the sample message payload
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageEvent {
    private String name;
    private String description;

}
