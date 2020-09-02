package com.myproject.springboot.logging;

import java.util.Calendar;

import org.testng.annotations.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyTest {

    
    @Test
    public void test1() {
        Calendar calendar = Calendar.getInstance();

        // the month starts at 0
        calendar.set(2001,3,16);

        log.info(String.format("%tF", calendar.getTime()));
    }
}
