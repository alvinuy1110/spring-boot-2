package com.myproject.springboot.jpa.controller;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.myproject.springboot.jpa.domain.ErrorDetails;
import com.myproject.springboot.jpa.service.ResourceNotFoundException;

/*
This is a global exception handler when Exceptions are thrown for the @Controller classes

Things to note:

* @ControllerAdvice - tells spring to apply this error handling across the @Controller classes
* The order or the method matters.  The Exception will go through each method, and will only call the first one that
* is applicable
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /*
    @ExceptionHandler - if the specific Exception (or its subclass matches, execute the method)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globleExcpetionHandler(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
