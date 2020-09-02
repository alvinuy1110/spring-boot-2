package com.myproject.springboot.validation.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.myproject.springboot.validation.domain.ApiError;

import static java.lang.String.format;
import static java.util.Objects.nonNull;

/*
This is a global exception handler when Exceptions are thrown for the @Controller classes

Things to note:

* @ControllerAdvice - tells spring to apply this error handling across the @Controller classes
* The order or the method matters.  The Exception will go through each method, and will only call the first one that
* is applicable
 */
@ControllerAdvice
public class GlobalExceptionHandler {


    // This is thrown when used in @RequestMapping (@Valid....)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentException(MethodArgumentNotValidException ex) {
        // example of assembling manually
        List<String> errors = new ArrayList<>();

        String errorMessage = ex.getMessage();
        BindingResult bindingResult = ex.getBindingResult();
        if (bindingResult.hasFieldErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (nonNull(fieldError)) {
                errorMessage = format("[%s] for parameter [%s] is not a valid argument. Details: %s",
                        fieldError.getRejectedValue(),
                        fieldError.getField(),
                        fieldError.getDefaultMessage());
                errors.add(errorMessage);
            }
        }
        ApiError apiError = new ApiError(new Date(), ex.getMessage(), errors,null);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
    
    // This is thrown when used in @Validated
    /*
    @ExceptionHandler - if the specific Exception (or its subclass matches, execute the method)
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<?> constraintViolationException(ConstraintViolationException ex, WebRequest request) {

        // example of assembling manually
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }

        ApiError apiError = new ApiError(new Date(), ex.getMessage(), errors, ex.getConstraintViolations());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceeptionHandler(Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(new Date(), ex.getMessage(), null, null);
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
