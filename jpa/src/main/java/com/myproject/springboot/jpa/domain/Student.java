package com.myproject.springboot.jpa.domain;

import java.util.Date;

import lombok.Data;

@Data
public class Student {

    private long id;
    private String firstName;
    private String lastName;
    private String studentNumber;
    private Date birthDate;
}
