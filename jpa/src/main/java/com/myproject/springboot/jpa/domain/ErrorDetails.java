package com.myproject.springboot.jpa.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This is sample of a DTO for errors when returned through the API
 */
@Getter
@AllArgsConstructor
public class ErrorDetails {

    private Date timestamp;
    private String message;
    private String details;

}
