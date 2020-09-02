package com.myproject.springboot.validation.domain;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * This is sample of a DTO for errors when returned through the API
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApiError {

    private Date timestamp;
    private String message;
    private List<String> details;

    private Set<ConstraintViolation<?>> violations;

}
