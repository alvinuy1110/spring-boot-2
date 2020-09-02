package com.myproject.springboot.validation.domain;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class Subject {

    @NotBlank
    private String name;

    @DecimalMin("0.00")
    @DecimalMax("5.00")
    private double grade;
}
