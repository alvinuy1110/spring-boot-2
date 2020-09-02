package com.myproject.springboot.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.myproject.springboot.web.property.MyProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * This is just a simple controller example
 */
@Controller
@Slf4j
public class TracingController {

    @Autowired
    private MyProperties properties;

    @RequestMapping(value = "/mytrace", method = RequestMethod.GET)
    public ResponseEntity<MyProperties> getProperties() {
        // for demo only
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        Map<String, String> params = new HashMap<>();

        log.info("sending request");
        HttpEntity entity = new HttpEntity(headers);
        String url ="http://www.yahoo.com";
        ResponseEntity<String> response  = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, params);

        log.info("sending 2nd request");
        url ="http://www.google.com";
        response  = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, params);
        log.info("sending respopnse");
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }


}
