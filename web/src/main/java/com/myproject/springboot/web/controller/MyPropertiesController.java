package com.myproject.springboot.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.myproject.springboot.web.property.MyProperties;
import com.myproject.springboot.web.service.MyService;
import lombok.extern.slf4j.Slf4j;

/**
 * This is just a simple controller example
 */
@Controller
@Slf4j
public class MyPropertiesController {

    @Autowired
    private MyService myService;

    @RequestMapping(value = "/myprops", method = RequestMethod.GET)
    public ResponseEntity<MyProperties> getProperties() {

        return new ResponseEntity<>(myService.getProperties(), HttpStatus.OK);
    }

    @RequestMapping(value = "/myprops", method = RequestMethod.POST)
    public ResponseEntity<MyProperties> postProperties(@RequestBody MyProperties properties) {
        // fake call
        this.myService.getProperties();
        return new ResponseEntity<>(properties, HttpStatus.CREATED);
    }

}
