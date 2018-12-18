package com.myproject.springboot.web.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

@Component
public class MyHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        Status status = checkStatus(); // perform some specific health check
        return Health.status(status).build();
    }

    protected int check() {

        // TODO
        return 0;
    }
    protected Status checkStatus() {

        Status status=null;

        // toggle option for testing/ demo
        //status= getUpStatus();
//        status= getFatalStatus();
        status= getUnknownStatus();

        return status;
    }

    protected Status getUpStatus() {
        return Status.UP;
    }
    protected Status getUnknownStatus() {
        return Status.UNKNOWN;
    }
    protected Status getFatalStatus() {
        Status status= new Status("FATAL", "fatal error");
        return status;
    }
}