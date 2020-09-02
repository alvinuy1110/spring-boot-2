package com.myproject.springboot.web.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.myproject.springboot.web.property.MyProperties;

/**
 * Created by user on 28/05/19.
 */
public class MyService {

    @Autowired
    private MyProperties properties;

    public MyProperties getProperties() {
        return properties;
    }
}
